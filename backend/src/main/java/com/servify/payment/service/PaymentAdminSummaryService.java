package com.servify.payment.service;

import com.servify.booking.model.BookingEntity;
import com.servify.booking.repository.BookingRepository;
import com.servify.payment.dto.ProviderRevenueSummaryResponse;
import com.servify.payment.model.PaymentStatus;
import com.servify.payment.model.PaymentTransaction;
import com.servify.payment.repository.PaymentTransactionRepository;
import com.servify.provider.model.ProviderEntity;
import lombok.RequiredArgsConstructor;
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
public class PaymentAdminSummaryService {

    private static final BigDecimal PLATFORM_FEE_RATE = new BigDecimal("0.10");

    private final PaymentTransactionRepository transactionRepository;
    private final BookingRepository bookingRepository;

    public List<ProviderRevenueSummaryResponse> getProviderRevenueSummary() {
        List<PaymentTransaction> transactions = transactionRepository.findByStatus(PaymentStatus.SUCCEEDED);
        if (transactions == null || transactions.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> orderIds = transactions.stream()
            .map(PaymentTransaction::getOrderId)
            .toList();

        Map<Long, BookingEntity> bookingMap = bookingRepository.findAllById(orderIds).stream()
            .collect(Collectors.toMap(BookingEntity::getBookingId, Function.identity()));

        Map<Long, RevenueAccumulator> revenueByProvider = transactions.stream()
            .map(transaction -> toAccumulatorEntry(transaction, bookingMap.get(transaction.getOrderId())))
            .filter(entry -> entry != null)
            .collect(Collectors.toMap(
                RevenueAccumulator::providerId,
                Function.identity(),
                RevenueAccumulator::merge
            ));

        return revenueByProvider.values().stream()
            .map(RevenueAccumulator::toResponse)
            .sorted((a, b) -> Long.compare(b.getGrossAmount(), a.getGrossAmount()))
            .toList();
    }

    private RevenueAccumulator toAccumulatorEntry(PaymentTransaction transaction, BookingEntity booking) {
        if (booking == null || booking.getProvider() == null) {
            return null;
        }
        ProviderEntity provider = booking.getProvider();
        long grossAmount = transaction.getAmount();
        long feeAmount = calculateFee(grossAmount);
        return new RevenueAccumulator(
            provider.getUserId(),
            provider.getName(),
            grossAmount,
            grossAmount - feeAmount,
            feeAmount,
            transaction.getCurrency()
        );
    }

    private long calculateFee(long amount) {
        return BigDecimal.valueOf(amount)
            .multiply(PLATFORM_FEE_RATE)
            .setScale(0, RoundingMode.HALF_UP)
            .longValue();
    }

    private static class RevenueAccumulator {
        private final Long providerId;
        private final String providerName;
        private long grossAmount;
        private long netAmount;
        private long feeAmount;
        private String currency;

        RevenueAccumulator(Long providerId, String providerName, long grossAmount, long netAmount, long feeAmount, String currency) {
            this.providerId = providerId;
            this.providerName = providerName;
            this.grossAmount = grossAmount;
            this.netAmount = netAmount;
            this.feeAmount = feeAmount;
            this.currency = currency;
        }

        static RevenueAccumulator merge(RevenueAccumulator first, RevenueAccumulator second) {
            first.grossAmount += second.grossAmount;
            first.netAmount += second.netAmount;
            first.feeAmount += second.feeAmount;
            return first;
        }

        Long providerId() {
            return providerId;
        }

        ProviderRevenueSummaryResponse toResponse() {
            return ProviderRevenueSummaryResponse.builder()
                .providerId(providerId)
                .providerName(providerName)
                .grossAmount(grossAmount)
                .providerNetAmount(netAmount)
                .platformFeeAmount(feeAmount)
                .currency(currency)
                .build();
        }
    }
}
