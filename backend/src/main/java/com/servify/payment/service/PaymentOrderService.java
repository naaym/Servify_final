package com.servify.payment.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.model.BookingStatus;
import com.servify.booking.repository.BookingRepository;
import com.servify.payment.config.StripeProperties;
import com.servify.provider.model.ProviderEntity;
import com.servify.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PaymentOrderService {

    private final BookingRepository bookingRepository;
    private final StripeProperties stripeProperties;

    public BigDecimal resolveAmount(Long orderId) {
        BookingEntity booking = bookingRepository.findById(orderId)
            .orElseThrow(() -> new ResourceNotFoundException("Booking not found for orderId " + orderId));
        if (booking.getStatus() != BookingStatus.ACCEPTED) {
            throw new IllegalArgumentException("Booking must be accepted before payment.");
        }
        ProviderEntity provider = booking.getProvider();
        if (provider != null && provider.getBasePrice() != null) {
            return BigDecimal.valueOf(provider.getBasePrice());
        }
        return stripeProperties.getAmountDefault();
    }
}
