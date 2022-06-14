package com.goapi.goapi.controller.controllers.payments;

import com.goapi.goapi.controller.forms.payments.AppServicePaymentData;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.service.interfaces.facade.finances.PaymentsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author Daniil Dmitrochenkov
 **/
@RestController
@RequestMapping("/user/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentsFacade paymentsFacade;

    @PostMapping("/service")
    public ResponseEntity makePaymentToAppService(@AuthenticationPrincipal User user,
                                                  @Valid @RequestBody AppServicePaymentData appServicePaymentData) {
        paymentsFacade.makeAppServicePayment(user, appServicePaymentData);
        return ResponseEntity.ok().build();
    }

}
