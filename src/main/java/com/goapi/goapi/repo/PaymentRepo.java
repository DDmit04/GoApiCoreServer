package com.goapi.goapi.repo;

import com.goapi.goapi.domain.Payment;
import com.goapi.goapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepo extends JpaRepository<Payment, Integer> {

}
