package com.goapi.goapi.service.implementation.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.finances.bill.userBill.UserBillNotFoundException;
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
        return userBillRepository.save(userBill);
    }
    @Override
    public UserBill getUserBill(User user) {
        Integer userId = user.getId();
        Optional<UserBill> userBill = userBillRepository.findByUser_Id(userId);
        return userBill.orElseThrow(() -> new UserBillNotFoundException(userId));
    }

    @Override
    public UserBill getUserBillWithPayments(User user) {
        Integer userId = user.getId();
        Optional<UserBill> userBill = userBillRepository.findByUserIdWithPayments(userId);
        return userBill.orElseThrow(() -> new UserBillNotFoundException(userId));
    }

}
