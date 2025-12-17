package com.servify.booking.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookingStatsResponse {
    private long totalRequests;
    private long totalPending;
    private long totalAccepted;
    private long totalRejected;
    private long totalCancelled;
    private long totalDone;
}
