package com.servify.payment.controller;

import com.servify.payment.config.StripeProperties;
import com.servify.payment.dto.PaymentConfigResponse;
import com.servify.payment.dto.PaymentHistoryItemResponse;
import com.servify.payment.dto.PaymentIntentRequest;
import com.servify.payment.dto.PaymentIntentResponse;
import com.servify.payment.dto.ProviderRevenueSummaryResponse;
import com.servify.payment.service.PaymentAdminSummaryService;
import com.servify.payment.service.PaymentHistoryService;
import com.servify.payment.service.PaymentIntentService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentIntentService paymentIntentService;
    private final StripeProperties stripeProperties;
    private final PaymentHistoryService paymentHistoryService;
    private final PaymentAdminSummaryService paymentAdminSummaryService;

    @PostMapping("/intents")
    public ResponseEntity<PaymentIntentResponse> createIntent(@Valid @RequestBody PaymentIntentRequest request) throws StripeException {
        return ResponseEntity.ok(paymentIntentService.createPaymentIntent(request.getOrderId()));
    }

    @GetMapping("/config")
    public ResponseEntity<PaymentConfigResponse> getConfig() {
        return ResponseEntity.ok(
            PaymentConfigResponse.builder()
                .publishableKey(stripeProperties.getPublishableKey())
                .currency(stripeProperties.getCurrency())
                .defaultAmount(stripeProperties.getAmountDefault().toPlainString())
                .build()
        );
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/history")
    public ResponseEntity<List<PaymentHistoryItemResponse>> getClientHistory() {
        return ResponseEntity.ok(paymentHistoryService.getClientHistory());
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider/history")
    public ResponseEntity<List<PaymentHistoryItemResponse>> getProviderHistory() {
        return ResponseEntity.ok(paymentHistoryService.getProviderHistory());
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @GetMapping("/superadmin/summary")
    public ResponseEntity<List<ProviderRevenueSummaryResponse>> getProviderRevenueSummary() {
        return ResponseEntity.ok(paymentAdminSummaryService.getProviderRevenueSummary());
    }
}
