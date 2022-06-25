package com.goapi.goapi.service.interfaces.facade.finances;

import com.goapi.goapi.domain.dto.finances.userBIll.UserBillDto;
import com.goapi.goapi.domain.model.user.User;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserBillServiceFacade {

    UserBillDto getUserBillDto(User user);
}
