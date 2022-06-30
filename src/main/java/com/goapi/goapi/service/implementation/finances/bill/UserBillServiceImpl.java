package com.goapi.goapi.service.implementation.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.finances.bill.UserBillNotFoundException;
import com.goapi.goapi.repo.finances.bill.UserBillRepository;
import com.goapi.goapi.service.interfaces.finances.bill.UserBillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class UserBillServiceImpl implements UserBillService {

    private final UserBillRepository userBillRepository;

    @Override
    public UserBill createUserBill() {
        UserBill userBill = new UserBill();
        return userBill;
    }
    @Override
    public UserBill getUserBill(User user) {
        Integer bullId = user.getUserBill().getId();
        Optional<UserBill> userBill = userBillRepository.findById(bullId);
        return userBill.orElseThrow(() -> new UserBillNotFoundException(bullId));
    }

    @Override
    public UserBill getUserBillWithPayments(User user) {
        Integer billId = user.getUserBill().getId();
        Optional<UserBill> userBill = userBillRepository.findByUserIdWithPayments(billId);
        return userBill.orElseThrow(() -> new UserBillNotFoundException(billId));
    }

}
