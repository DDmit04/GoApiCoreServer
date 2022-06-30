package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AppServiceBillRepository extends JpaRepository<AppServiceBill, Integer> {

    @Query("from AppServiceBill bill where bill.appServiceObject.id = :appServiceId and bill.appServiceObject.owner.id = :userId")
    @EntityGraph("AppServiceBill.payments")
    Optional<AppServiceBill> findByIdAndUserIdWithAllPayments(Integer userId, Integer appServiceId);
}