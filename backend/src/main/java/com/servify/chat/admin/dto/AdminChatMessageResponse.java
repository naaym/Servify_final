package com.servify.chat.admin.dto;

import com.servify.user.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminChatMessageResponse {
    private Long messageId;
    private Long threadId;
    private Long senderId;
    private Role senderRole;
    private String senderName;
    private String senderImageUrl;
    private String content;
    private long createdAt;
}
