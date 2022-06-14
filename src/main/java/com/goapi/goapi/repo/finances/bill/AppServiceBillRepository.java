package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.BillType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppServiceBillRepository extends JpaRepository<AppServiceBill, Integer> {


    Optional<AppServiceBill> findByUser_IdAndBillType(Integer userId, BillType billType);

}