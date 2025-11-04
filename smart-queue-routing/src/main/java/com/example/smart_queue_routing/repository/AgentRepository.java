package com.example.smart_queue_routing.repository;

import com.example.smart_queue_routing.model.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

public interface AgentRepository extends JpaRepository<Agent, UUID> {
    List<Agent> findByAvailableTrue();
}
