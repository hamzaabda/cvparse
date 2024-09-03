package com.example.pfe.controllers;

import com.example.pfe.models.Message;
import com.example.pfe.services.ChatService;
import com.example.pfe.services.WebSocketSessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ChatService chatService;

    public ChatWebSocketHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        chatService.addSession(session.getId(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        chatService.removeSession(session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        // Parse the incoming message
        String payload = message.getPayload();
        // Deserialize payload into Message object
        Message msg = new ObjectMapper().readValue(payload, Message.class);
        msg.setTimestamp(String.valueOf(System.currentTimeMillis())); // Set timestamp as current time

        // Broadcast the message to all sessions
        chatService.broadcastMessage(msg);
    }
}