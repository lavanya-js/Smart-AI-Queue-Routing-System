package com.example.smart_queue_routing.controller;

import com.example.smart_queue_routing.model.Request;
import com.example.smart_queue_routing.repository.RequestRepository;
import com.example.smart_queue_routing.service.RoutingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/requests")
@CrossOrigin(origins = "*")
public class RequestController {

    private final RequestRepository requestRepo;
    private final RoutingService routingService;

    @Autowired
    public RequestController(RequestRepository requestRepo, RoutingService routingService) {
        this.requestRepo = requestRepo;
        this.routingService = routingService;
    }

    @GetMapping
    public List<Request> getAllRequests() {
        return requestRepo.findAll();
    }

    @PostMapping("/assign")
    public Request assignRequest(@RequestBody Request req) {
        return routingService.autoAssign(req);
    }
}
