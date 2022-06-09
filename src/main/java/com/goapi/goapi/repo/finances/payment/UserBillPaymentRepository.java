package com.goapi.goapi.repo.finances.payment;

import com.goapi.goapi.domain.model.payment.UserBillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBillPaymentRepository extends JpaRepository<UserBillPayment, Long> {
}