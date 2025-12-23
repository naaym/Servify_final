package com.servify.chat.admin.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AdminChatAdminResponse {
    private Long adminId;
    private String name;
    private String email;
    private String profileImageUrl;
}
