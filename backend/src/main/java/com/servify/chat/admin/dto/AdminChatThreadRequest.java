package com.servify.chat.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminChatThreadRequest {
    @NotNull
    private Long adminId;
}
