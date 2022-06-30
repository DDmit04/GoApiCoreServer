package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserBillRepository extends JpaRepository<UserBill, Integer> {

    Optional<UserBill> findById(Integer billId);

    @Query("from UserBill ub where  ub.id = :billId")
    @EntityGraph("UserBill.payments")
    Optional<UserBill> findByUserIdWithPayments(@Param("billId") Integer billId);
}