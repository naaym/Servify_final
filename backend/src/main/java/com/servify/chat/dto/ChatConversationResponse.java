package com.servify.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatConversationResponse {
    private Long bookingId;
    private String participantName;
    private String participantImageUrl;
    private String lastMessage;
    private Long lastMessageAt;
}
