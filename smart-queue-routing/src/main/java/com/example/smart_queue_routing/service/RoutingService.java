package com.example.smart_queue_routing.service;

import com.example.smart_queue_routing.model.Agent;
import com.example.smart_queue_routing.model.Request;
import com.example.smart_queue_routing.repository.AgentRepository;
import com.example.smart_queue_routing.repository.RequestRepository;
import org.encog.ml.data.basic.BasicMLData;
import org.encog.neural.networks.BasicNetwork;
import org.encog.neural.networks.layers.BasicLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoutingService {

    private final AgentRepository agentRepo;
    private final RequestRepository requestRepo;
    private final SimpMessagingTemplate messagingTemplate;
    private BasicNetwork network;

    @Autowired
    public RoutingService(AgentRepository agentRepo,
                          RequestRepository requestRepo,
                          SimpMessagingTemplate messagingTemplate) {
        this.agentRepo = agentRepo;
        this.requestRepo = requestRepo;
        this.messagingTemplate = messagingTemplate;
    }

    // 🧩 Initialize AI neural network
    private void initNetwork() {
        if (network != null) return;

        network = new BasicNetwork();
        network.addLayer(new BasicLayer(null, true, 5));  // input layer
        network.addLayer(new BasicLayer(null, true, 3));  // hidden layer
        network.addLayer(new BasicLayer(null, false, 1)); // output layer
        network.getStructure().finalizeStructure();
        network.reset();
    }

    // 🚀 Main auto-assignment logic
    public Request autoAssign(Request req) {
        initNetwork();

        List<Agent> availableAgents = agentRepo.findByAvailableTrue();
        Agent best = null;
        double bestScore = -1;

        for (Agent a : availableAgents) {
            double skillMatch = topicMatch(a, req);
            double availability = agentAvailability(a, req.getChannel());
            double priority = req.getPriority() / 10.0;
            double pcs = a.getAvgPCS() / 5.0;
            double timeFactor = 1.0 / (1.0 + a.getAvgPHT() / 300.0);

            double[] input = {skillMatch, availability, priority, pcs, timeFactor};
            double[] output = network.compute(new BasicMLData(input)).getData();
            double result = output[0];

            if (result > bestScore) {
                bestScore = result;
                best = a;
            }
        }

        if (best != null) {
            // ✅ Update agent load
            if ("Chat".equalsIgnoreCase(req.getChannel()))
                best.setActiveChats(best.getActiveChats() + 1);
            else
                best.setActiveCalls(best.getActiveCalls() + 1);

            if (best.getActiveChats() >= best.getMaxChats() || best.getActiveCalls() >= best.getMaxCalls())
                best.setAvailable(false);

            agentRepo.save(best);

            // ✅ Assign request details
            req.setAssignedAgentId(best.getId());
            req.setStatus("Assigned");
            req.setSessionId(UUID.randomUUID().toString());

            Request savedReq = requestRepo.save(req);

            // 💬 Notify frontend via WebSocket
            messagingTemplate.convertAndSend("/topic/updates", savedReq);

            return savedReq;
        }

        // No agent available case
        req.setStatus("Pending");
        Request savedPending = requestRepo.save(req);
        messagingTemplate.convertAndSend("/topic/updates", savedPending);
        return savedPending;
    }

    // 🧩 Helper methods
    private double topicMatch(Agent a, Request r) {
        return a.getSpecialty().equalsIgnoreCase(r.getTopic()) ? 1.0 : 0.5;
    }

    private double agentAvailability(Agent a, String channel) {
        if ("Chat".equalsIgnoreCase(channel))
            return a.getActiveChats() < a.getMaxChats() ? 1.0 : 0.0;
        return a.getActiveCalls() < a.getMaxCalls() ? 1.0 : 0.0;
    }
}
