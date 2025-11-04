package com.example.smart_queue_routing.repository;

import com.example.smart_queue_routing.model.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface RequestRepository extends JpaRepository<Request, UUID> {}
