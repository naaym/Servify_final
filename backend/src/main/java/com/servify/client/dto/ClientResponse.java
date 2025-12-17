package com.servify.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
@Getter
@Setter
@NoArgsConstructor
public class ClientResponse {

    private Long id;
    private String name;
    private String email;
    private String phone;
    private String governorate;
    private String profileImageUrl;
    private Instant createdAt;


}
