package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.bill.AppServiceBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppServiceBillRepository extends JpaRepository<AppServiceBill, Integer> {
}