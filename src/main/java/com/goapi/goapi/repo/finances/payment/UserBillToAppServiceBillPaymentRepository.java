package com.goapi.goapi.repo.finances.payment;

import com.goapi.goapi.domain.model.payment.UserBillToAppServiceBillPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBillToAppServiceBillPaymentRepository extends JpaRepository<UserBillToAppServiceBillPayment, Long> {
}