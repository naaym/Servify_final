package com.servify.booking.repository;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    List<BookingEntity> findByClientUserIdOrderByCreatedAtDesc(Long clientId);
    Optional<BookingEntity> findByBookingIdAndClientUserId(Long bookingId, Long clientId);
    long countByClientUserId(Long clientId);
    long countByClientUserIdAndStatus(Long clientId, BookingStatus status);
}
