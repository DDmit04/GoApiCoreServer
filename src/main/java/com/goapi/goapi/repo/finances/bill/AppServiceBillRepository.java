package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AppServiceBillRepository extends JpaRepository<AppServiceBill, Integer> {

    String selectAppServiceBillByIdQuery = "from AppServiceBill bill where bill.id = :appServiceBillId";


    @Query(selectAppServiceBillByIdQuery)
    @EntityGraph("AppServiceBill.user")
    Optional<AppServiceBill> findByIdWithOwner(@Param("appServiceBillId") Integer appServiceBillId);

    @Query(selectAppServiceBillByIdQuery)
    @EntityGraph("AppServiceBill.payments")
    Optional<AppServiceBill> findByIdWithOwnerAndPayments(@Param("appServiceBillId")Integer appServiceBillId);

    Optional<AppServiceBill> findByAppServiceObject_Id(Integer appServiceId);
}