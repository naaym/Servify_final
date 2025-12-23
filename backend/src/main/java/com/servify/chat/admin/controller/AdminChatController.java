package com.servify.chat.admin.controller;

import com.servify.chat.admin.dto.AdminChatAdminResponse;
import com.servify.chat.admin.dto.AdminChatConversationResponse;
import com.servify.chat.admin.dto.AdminChatMessageResponse;
import com.servify.chat.admin.dto.AdminChatThreadRequest;
import com.servify.chat.admin.service.AdminChatService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin-chats")
@RequiredArgsConstructor
public class AdminChatController {

    private final AdminChatService adminChatService;

    @GetMapping("/threads")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','PROVIDER','SUPER_ADMIN')")
    public ResponseEntity<List<AdminChatConversationResponse>> getConversations() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(adminChatService.getConversations(email));
    }

    @GetMapping("/threads/{threadId}/messages")
    @PreAuthorize("hasAnyRole('ADMIN','CLIENT','PROVIDER','SUPER_ADMIN')")
    public ResponseEntity<List<AdminChatMessageResponse>> getMessages(@PathVariable("threadId") Long threadId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(adminChatService.getMessages(threadId, email));
    }

    @PostMapping("/threads")
    @PreAuthorize("hasAnyRole('CLIENT','PROVIDER','SUPER_ADMIN')")
    public ResponseEntity<AdminChatConversationResponse> createThread(
            @Valid @RequestBody AdminChatThreadRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(adminChatService.createThread(request.getAdminId(), email));
    }

    @GetMapping("/admins")
    @PreAuthorize("hasAnyRole('CLIENT','PROVIDER','SUPER_ADMIN')")
    public ResponseEntity<List<AdminChatAdminResponse>> getAdmins() {
        return ResponseEntity.ok(adminChatService.getAvailableAdmins());
    }
}
