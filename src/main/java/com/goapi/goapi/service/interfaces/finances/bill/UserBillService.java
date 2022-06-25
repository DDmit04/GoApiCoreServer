package com.goapi.goapi.service.interfaces.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserBillService {

    UserBill createUserBill();
    UserBill getUserBill(User user);
    UserBill getUserBillWithPayments(User user);
}
