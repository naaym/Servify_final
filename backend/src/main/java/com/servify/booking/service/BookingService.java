package com.servify.booking.service;

import com.servify.booking.dto.BookingDetailsResponse;
import com.servify.booking.dto.BookingResponse;
import com.servify.booking.dto.BookingStatsResponse;
import com.servify.booking.model.BookingStatus;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(Long providerId, String date, String time, String description, List<MultipartFile> attachments);
    List<BookingResponse> getMyBookings();
    BookingStatsResponse getMyStats();
    BookingDetailsResponse getBookingDetails(Long bookingId);
    BookingDetailsResponse cancelBooking(Long bookingId);
    List<BookingResponse> getProviderBookings();
    BookingDetailsResponse getProviderBookingDetails(Long bookingId);
    BookingDetailsResponse updateStatusAsProvider(Long bookingId, BookingStatus status);
}
