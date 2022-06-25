package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.UserPayment;
import com.goapi.goapi.repo.finances.payment.UserBillPaymentRepository;
import com.goapi.goapi.service.interfaces.finances.payment.UserPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserPaymentServiceImpl implements UserPaymentService {

    private final UserBillPaymentRepository userBillPaymentRepository;

    @Override
    public UserPayment createUserBillPayment(BigDecimal sum, String description, UserBill userBill) {
        UserPayment newUserPayment = new UserPayment(sum, description, userBill);
        newUserPayment = userBillPaymentRepository.save(newUserPayment);
        return newUserPayment;
    }

}
