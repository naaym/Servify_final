package com.servify.booking.dto;

import com.servify.booking.model.BookingStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingResponse {
    private Long bookingId;
    private BookingStatus status;
    private String category;
    private String providerName;
    private String clientName;
    private Long date;
}
