package com.goapi.goapi.repo.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.AppServicePayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppServiceBillPaymentRepository extends JpaRepository<AppServicePayment, Integer> {
}