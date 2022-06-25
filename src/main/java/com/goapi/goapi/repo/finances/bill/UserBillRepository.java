package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserBillRepository extends JpaRepository<UserBill, Integer> {

    Optional<UserBill> findByUser_Id(Integer userId);

    @Query(nativeQuery = true, value = "select * from user_bill bill where bill.user_id = :userId")
    @EntityGraph("UserBill.payments")
    Optional<UserBill> findByUserIdWithPayments(@Param("userId") Integer userId);
}