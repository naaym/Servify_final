package com.servify.chat.admin.dto;

import com.servify.user.enums.Role;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminChatConversationResponse {
    private Long threadId;
    private String participantName;
    private String participantImageUrl;
    private String lastMessage;
    private long lastMessageAt;
    private Role participantRole;
}
