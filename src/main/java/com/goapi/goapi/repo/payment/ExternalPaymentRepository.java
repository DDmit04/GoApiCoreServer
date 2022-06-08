package com.goapi.goapi.repo.payment;

import com.goapi.goapi.domain.model.payment.ExternalPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExternalPaymentRepository extends JpaRepository<ExternalPayment, Long> {
}