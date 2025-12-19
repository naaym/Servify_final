package com.servify.chat.service;

import com.servify.chat.dto.ChatMessageResponse;

import java.util.List;

public interface ChatMessageService {
    ChatMessageResponse sendMessage(Long bookingId, String content, String senderEmail);
    List<ChatMessageResponse> getMessages(Long bookingId, String requesterEmail);
}
