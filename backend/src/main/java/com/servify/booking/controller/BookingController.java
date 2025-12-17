package com.servify.booking.controller;

import com.servify.booking.dto.BookingDetailsResponse;
import com.servify.booking.dto.BookingResponse;
import com.servify.booking.dto.BookingStatsResponse;
import com.servify.booking.dto.BookingStatusUpdateRequest;
import com.servify.booking.model.BookingStatus;
import com.servify.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestParam("providerId") Long providerId,
            @RequestParam("date") String date,
            @RequestParam("time") String time,
            @RequestParam("description") String description,
            @RequestPart(value = "images", required = false) List<MultipartFile> attachments
    ) {
        return ResponseEntity.ok(bookingService.createBooking(providerId, date, time, description, attachments));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<BookingResponse>> getMyBookings() {
        return ResponseEntity.ok(bookingService.getMyBookings());
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/stats")
    public ResponseEntity<BookingStatsResponse> getMyStats() {
        return ResponseEntity.ok(bookingService.getMyStats());
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<BookingDetailsResponse> getBookingDetails(@PathVariable("id") Long bookingId) {
        return ResponseEntity.ok(bookingService.getBookingDetails(bookingId));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BookingDetailsResponse> updateStatus(
            @PathVariable("id") Long bookingId,
            @RequestBody BookingStatusUpdateRequest request
    ) {
        BookingStatus status = request.getStatus();
        return ResponseEntity.ok(bookingService.updateStatus(bookingId, status));
    }
}
