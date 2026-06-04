package com.example.demo700.Configurations.ChatAndNotificationConfigurations;

import com.example.demo700.Services.Presence.UserPresenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketPresenceListener {

    @Autowired
    private UserPresenceService presenceService;

    @EventListener
    public void handleConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = accessor.getFirstNativeHeader("userId");

        if (userId != null) {
            accessor.getSessionAttributes().put("userId", userId);
            presenceService.userConnected(userId);
            System.out.println(userId + " ONLINE");
        }
    }

    @EventListener
    public void handleDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        String userId = (String) accessor.getSessionAttributes().get("userId");

        if (userId != null) {
            presenceService.userDisconnected(userId);
            System.out.println(userId + " OFFLINE");
        }
    }
}