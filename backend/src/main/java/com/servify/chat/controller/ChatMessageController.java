package com.servify.chat.controller;

import com.servify.chat.dto.ChatMessageResponse;
import com.servify.chat.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    @PreAuthorize("hasAnyRole('CLIENT','PROVIDER')")
    @GetMapping("/{id:\\d+}/messages")
    public ResponseEntity<List<ChatMessageResponse>> getMessages(@PathVariable("id") Long bookingId) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(chatMessageService.getMessages(bookingId, email));
    }
}
