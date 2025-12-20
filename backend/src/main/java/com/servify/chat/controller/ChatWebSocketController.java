package com.servify.chat.controller;

import com.servify.chat.dto.ChatMessageRequest;
import com.servify.chat.dto.ChatMessageResponse;
import com.servify.chat.service.ChatMessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class ChatWebSocketController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.send")
    public void sendMessage(@Valid ChatMessageRequest request, Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Unauthorized");
        }
        ChatMessageResponse response = chatMessageService.sendMessage(request.getBookingId(), request.getContent(), principal.getName());
        messagingTemplate.convertAndSend("/topic/bookings/" + request.getBookingId(), response);
    }
}
