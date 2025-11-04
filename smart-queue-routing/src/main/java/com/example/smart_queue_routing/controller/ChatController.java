package com.example.smart_queue_routing.controller;

import com.example.smart_queue_routing.model.ChatMessage;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import java.time.LocalTime;

@Controller
public class ChatController {

    @MessageMapping("/send/{sessionId}")
    @SendTo("/topic/{sessionId}")
    public ChatMessage send(@DestinationVariable String sessionId, ChatMessage msg) {
        msg.setTime(LocalTime.now().toString());
        return msg;
    }
}
