package com.goapi.goapi.repo.finances.payment;

import com.goapi.goapi.domain.model.finances.payment.AppServicePayout;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppServicePayoutRepository extends JpaRepository<AppServicePayout, Integer> {
}