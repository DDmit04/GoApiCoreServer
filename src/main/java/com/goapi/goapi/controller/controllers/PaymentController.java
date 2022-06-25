package com.goapi.goapi.controller.controllers;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import com.goapi.goapi.domain.dto.finances.appServiceBill.AppServiceBillDto;
import com.goapi.goapi.domain.dto.finances.userBIll.UserBillDto;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.finances.AppServiceBillServiceFacade;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsServiceFacade;
import com.goapi.goapi.service.interfaces.facade.finances.UserBillServiceFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user/finances")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentsServiceFacade paymentsServiceFacade;
    private final AppServiceBillServiceFacade appServiceBillServiceFacade;
    private final UserBillServiceFacade userBillServiceFacade;

    @PostMapping("/payment")
    public ResponseEntity makePaymentToAppService(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody AppServicePaymentData appServicePaymentData) {
        paymentsServiceFacade.makeAppServicePayment(user, appServicePaymentData);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/bill/{appServiceId}")
    public ResponseEntity<AppServiceBillDto> getAppServiceBill(@AuthenticationPrincipal User user, @PathVariable Integer appServiceId) {
        AppServiceBillDto billDto = appServiceBillServiceFacade.getAppServiceBillDto(user, appServiceId);
        return ResponseEntity.ok(billDto);
    }

    @GetMapping("/bill")
    public ResponseEntity<UserBillDto> getUserBill(@AuthenticationPrincipal User user) {
        UserBillDto userBillDto = userBillServiceFacade.getUserBillDto(user);
        return ResponseEntity.ok(userBillDto);
    }
}
