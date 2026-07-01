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

        // Set by StompAuthChannelInterceptor after JWT validation
        String userId = accessor.getSessionAttributes() != null
                ? (String) accessor.getSessionAttributes().get("userId")
                : null;

        if (userId != null) {
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