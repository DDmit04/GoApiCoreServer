package com.goapi.goapi.repo.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBillPaymentRepository extends JpaRepository<UserPayment, Integer> {
}