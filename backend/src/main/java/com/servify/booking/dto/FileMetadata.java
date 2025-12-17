package com.servify.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileMetadata {
    private String url;
    private String name;
}
