package com.goapi.goapi.service.implementation;

import com.goapi.goapi.domain.model.bill.Bill;
import com.goapi.goapi.domain.model.bill.BillType;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.repo.payment.BillRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class BillService {

    private final BillRepository billRepository;

    public Bill createDatabaseBill(User owner) {
        Bill bill = createBill(owner, BillType.DATABASE);
        return bill;
    }
    public Bill createUserBill() {
        Bill bill = new Bill(BillType.USER);
        bill = billRepository.save(bill);
        return bill;
    }
    public Bill createUserApiBill(User owner) {
        Bill bill = createBill(owner, BillType.USER_API);
        return bill;
    }
    
    private Bill createBill(User owner, BillType billType) {
        Bill bill = new Bill(owner, billType);
        bill = billRepository.save(bill);
        return bill;
    }


}
