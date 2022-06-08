package com.goapi.goapi.repo.payment;

import com.goapi.goapi.domain.model.payment.InternalPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InternalPaymentRepository extends JpaRepository<InternalPayment, Long> {
}