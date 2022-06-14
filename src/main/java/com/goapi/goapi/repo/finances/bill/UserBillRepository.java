package com.goapi.goapi.repo.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserBillRepository extends JpaRepository<UserBill, Integer> {

    Optional<UserBill> findByUser_Id(Integer userId);

}