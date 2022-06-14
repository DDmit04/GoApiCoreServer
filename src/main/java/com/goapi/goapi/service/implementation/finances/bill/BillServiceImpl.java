package com.goapi.goapi.service.implementation.finances.bill;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.finances.bill.UserBill;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.exception.payments.AppServiceBillNotFoundException;
import com.goapi.goapi.exception.payments.UserBillNotFoundException;
import com.goapi.goapi.repo.finances.bill.AppServiceBillRepository;
import com.goapi.goapi.repo.finances.bill.UserBillRepository;
import com.goapi.goapi.service.interfaces.finances.bill.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class BillServiceImpl implements BillService {

    private final UserBillRepository userBillRepository;
    private final AppServiceBillRepository appServiceBillRepository;

    @Override
    public UserBill getUserBill(User user) {
        Integer userId = user.getId();
        Optional<UserBill> userBill = userBillRepository.findByUser_Id(userId);
        return userBill.orElseThrow(() -> new UserBillNotFoundException(userId));
    }

    @Override
    public AppServiceBill getAppServiceBillByIdWithOwner(Integer appServiceBillId) {
        Optional<AppServiceBill> appServiceBill = appServiceBillRepository.findById(appServiceBillId);
        return appServiceBill.orElseThrow(() -> new AppServiceBillNotFoundException(appServiceBillId));
    }

}
