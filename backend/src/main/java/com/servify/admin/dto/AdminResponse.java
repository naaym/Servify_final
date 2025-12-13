package com.servify.admin.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class AdminResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String governorate;
    private Instant createdAt;
}
