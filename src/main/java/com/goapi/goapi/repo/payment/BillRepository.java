package com.goapi.goapi.repo.payment;

import com.goapi.goapi.domain.model.bill.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Integer> {
}