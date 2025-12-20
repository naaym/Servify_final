package com.servify.chat.dto;

import com.servify.user.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatMessageResponse {
    private Long messageId;
    private Long bookingId;
    private Long senderId;
    private Role senderRole;
    private String senderName;
    private String senderImageUrl;
    private String content;
    private Long createdAt;
}
