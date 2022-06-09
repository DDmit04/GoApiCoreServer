package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.bill.UserBill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBillRepository extends JpaRepository<UserBill, Integer> {
}