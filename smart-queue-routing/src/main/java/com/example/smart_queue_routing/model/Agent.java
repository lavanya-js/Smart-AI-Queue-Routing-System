package com.example.smart_queue_routing.model;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Agent {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    private String name;
    private String specialty;
    private boolean available = true;
    private int maxChats = 3;
    private int maxCalls = 2;
    private int activeChats = 0;
    private int activeCalls = 0;
    private double avgPCS = 3.5;
    private double avgPHT = 120;

    public Agent() {}

    public Agent(String name, String specialty) {
        this.name = name;
        this.specialty = specialty;
    }

    // 💕 Getters and Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getSpecialty() { return specialty; }
    public void setSpecialty(String specialty) { this.specialty = specialty; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
    public int getMaxChats() { return maxChats; }
    public void setMaxChats(int maxChats) { this.maxChats = maxChats; }
    public int getMaxCalls() { return maxCalls; }
    public void setMaxCalls(int maxCalls) { this.maxCalls = maxCalls; }
    public int getActiveChats() { return activeChats; }
    public void setActiveChats(int activeChats) { this.activeChats = activeChats; }
    public int getActiveCalls() { return activeCalls; }
    public void setActiveCalls(int activeCalls) { this.activeCalls = activeCalls; }
    public double getAvgPCS() { return avgPCS; }
    public void setAvgPCS(double avgPCS) { this.avgPCS = avgPCS; }
    public double getAvgPHT() { return avgPHT; }
    public void setAvgPHT(double avgPHT) { this.avgPHT = avgPHT; }
}
