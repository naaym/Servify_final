package com.servify.chat.admin.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminChatMessageRequest {
    @NotNull
    private Long threadId;

    @NotBlank
    private String content;
}
