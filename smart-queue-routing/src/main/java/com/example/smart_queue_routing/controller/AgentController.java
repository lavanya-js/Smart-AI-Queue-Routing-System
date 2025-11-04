package com.example.smart_queue_routing.controller;

import com.example.smart_queue_routing.model.Agent;
import com.example.smart_queue_routing.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/agents")
@CrossOrigin(origins = "*")
public class AgentController {

    @Autowired
    private AgentRepository repo;

    @PostMapping
    public Agent add(@RequestBody Agent agent) {
        return repo.save(agent);


    }

    @GetMapping
    public List<Agent> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Agent getById(@PathVariable UUID id) {
        return repo.findById(id).orElse(null);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable UUID id) {
        repo.deleteById(id);
        return "Agent deleted successfully";
    }
}
