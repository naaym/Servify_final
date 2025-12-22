package com.servify.booking.dto;

import lombok.Data;

@Data
public class BookingUpdateRequest {
    private String date;
    private String time;
    private String description;
}
