package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.finances.payment.UserBillPayment;
import com.goapi.goapi.repo.finances.payment.UserBillPaymentRepository;
import com.goapi.goapi.service.interfaces.finances.payment.UserBillPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserBillPaymentServiceImpl implements UserBillPaymentService {

    private final UserBillPaymentRepository userBillPaymentRepository;

    @Override
    public UserBillPayment createUserBillPayment(BigDecimal sum, String description, UserBill userBill) {
        UserBillPayment newUserBillPayment = new UserBillPayment(sum, description, userBill);
        newUserBillPayment = userBillPaymentRepository.save(newUserBillPayment);
        return newUserBillPayment;
    }

}
