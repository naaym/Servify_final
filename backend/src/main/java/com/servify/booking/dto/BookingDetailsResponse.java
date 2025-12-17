package com.servify.booking.dto;

import com.servify.booking.model.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class BookingDetailsResponse {
    private BookingStatus status;
    private String description;
    private Long bookingId;
    private BookingStatus bookingStatus;
    private Long date;
    private Long updatedAt;
    private ProviderDetails providerInfo;
    private String serviceName;
    private String serviceGategory;
    private List<FileMetadata> attachments;
}
