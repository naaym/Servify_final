package com.servify.payment.repository;

import com.servify.payment.model.PaymentTransaction;
import com.servify.payment.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentTransactionRepository extends JpaRepository<PaymentTransaction, Long> {
    Optional<PaymentTransaction> findFirstByOrderIdOrderByCreatedAtDesc(Long orderId);
    Optional<PaymentTransaction> findByPaymentIntentId(String paymentIntentId);
    List<PaymentTransaction> findByOrderIdInOrderByCreatedAtDesc(List<Long> orderIds);
    List<PaymentTransaction> findByStatus(PaymentStatus status);
    boolean existsByOrderIdAndStatus(Long orderId, PaymentStatus status);
}
