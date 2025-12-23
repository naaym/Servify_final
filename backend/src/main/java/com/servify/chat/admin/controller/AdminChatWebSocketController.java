package com.servify.chat.admin.controller;

import com.servify.chat.admin.dto.AdminChatMessageRequest;
import com.servify.chat.admin.dto.AdminChatMessageResponse;
import com.servify.chat.admin.service.AdminChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AdminChatWebSocketController {

    private final AdminChatService adminChatService;
    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/admin-chat.send")
    public void sendMessage(@Valid AdminChatMessageRequest request, Principal principal) {
        if (principal == null) {
            throw new AccessDeniedException("Unauthorized");
        }
        AdminChatMessageResponse response = adminChatService.sendMessage(
                request.getThreadId(),
                request.getContent(),
                principal.getName()
        );
        messagingTemplate.convertAndSend("/topic/admin-chats/" + request.getThreadId(), response);
    }
}
