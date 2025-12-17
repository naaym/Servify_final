package com.servify.booking.dto;

import com.servify.booking.model.BookingStatus;
import lombok.Data;

@Data
public class BookingStatusUpdateRequest {
    private BookingStatus status;
}
