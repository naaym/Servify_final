package com.servify.payment.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.repository.BookingRepository;
import com.servify.client.model.ClientEntity;
import com.servify.client.repository.ClientRepository;
import com.servify.payment.dto.PaymentHistoryItemResponse;
import com.servify.payment.model.PaymentStatus;
import com.servify.payment.model.PaymentTransaction;
import com.servify.payment.repository.PaymentTransactionRepository;
import com.servify.provider.model.ProviderEntity;
import com.servify.provider.repository.ProviderRepository;
import com.servify.shared.exception.ResourceNotFoundException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentHistoryService {

    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.10");

    private final PaymentTransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;
    private final ClientRepository clientRepository;
    private final ProviderRepository providerRepository;

    public List<PaymentHistoryItemResponse> getClientHistory() {
        ClientEntity client = clientRepository.findByEmail(getCurrentUserEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
        List<BookingEntity> bookings = bookingRepository.findByClientUserIdOrderByCreatedAtDesc(client.getUserId());
        return mapHistoryForBookings(bookings, false);
    }

    public List<PaymentHistoryItemResponse> getProviderHistory() {
        ProviderEntity provider = providerRepository.findByEmail(getCurrentUserEmail())
            .orElseThrow(() -> new ResourceNotFoundException("Provider not found"));
        List<BookingEntity> bookings = bookingRepository.findByProviderUserIdOrderByCreatedAtDesc(provider.getUserId());
        return mapHistoryForBookings(bookings, true);
    }

    private List<PaymentHistoryItemResponse> mapHistoryForBookings(List<BookingEntity> bookings, boolean applyProviderShare) {
        if (bookings == null || bookings.isEmpty()) {
            return Collections.emptyList();
        }
        List<Long> bookingIds = bookings.stream()
            .map(BookingEntity::getBookingId)
            .toList();
        Map<Long, BookingEntity> bookingMap = bookings.stream()
            .collect(Collectors.toMap(BookingEntity::getBookingId, Function.identity()));

        List<PaymentTransaction> transactions = transactionRepository.findByOrderIdInOrderByCreatedAtDesc(bookingIds);
        refreshPendingStatuses(transactions);
        return transactions.stream()
            .map(transaction -> toResponse(transaction, bookingMap.get(transaction.getOrderId()), applyProviderShare))
            .toList();
    }

    private PaymentHistoryItemResponse toResponse(PaymentTransaction transaction, BookingEntity booking, boolean applyProviderShare) {
        String providerName = booking != null && booking.getProvider() != null
            ? booking.getProvider().getName()
            : null;
        String clientName = booking != null && booking.getClient() != null
            ? booking.getClient().getName()
            : null;
        Long amount = transaction.getAmount();
        if (applyProviderShare) {
            amount = applyProviderShare(amount);
        }

        return PaymentHistoryItemResponse.builder()
            .bookingId(transaction.getOrderId())
            .paymentIntentId(transaction.getPaymentIntentId())
            .amount(amount)
            .currency(transaction.getCurrency())
            .status(transaction.getStatus())
            .createdAt(transaction.getCreatedAt())
            .providerName(providerName)
            .clientName(clientName)
            .bookingDate(booking != null ? booking.getCreatedAt() : null)
            .build();
    }

    private long applyProviderShare(long amount) {
        long fee = BigDecimal.valueOf(amount)
            .multiply(PLATFORM_FEE_RATE)
            .setScale(0, RoundingMode.HALF_UP)
            .longValue();
        return amount - fee;
    }

    private String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    private void refreshPendingStatuses(List<PaymentTransaction> transactions) {
        for (PaymentTransaction transaction : transactions) {
            if (transaction.getStatus() != PaymentStatus.PENDING) {
                continue;
            }
            try {
                PaymentIntent intent = PaymentIntent.retrieve(transaction.getPaymentIntentId());
                PaymentStatus resolvedStatus = mapStripeStatus(intent.getStatus());
                if (resolvedStatus != transaction.getStatus()) {
                    transaction.setStatus(resolvedStatus);
                    transactionRepository.save(transaction);
                }
            } catch (StripeException exception) {
                log.warn("Unable to refresh payment status for intent {}", transaction.getPaymentIntentId(), exception);
            }
        }
    }

    private PaymentStatus mapStripeStatus(String stripeStatus) {
        return switch (stripeStatus) {
            case "succeeded" -> PaymentStatus.SUCCEEDED;
            case "canceled" -> PaymentStatus.FAILED;
            case "requires_payment_method",
                "requires_confirmation",
                "requires_action",
                "processing",
                "requires_capture" -> PaymentStatus.PENDING;
            default -> PaymentStatus.PENDING;
        };
    }
}
