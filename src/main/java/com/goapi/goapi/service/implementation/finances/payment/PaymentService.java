package com.goapi.goapi.service.implementation.finances.payment;

import com.goapi.goapi.domain.dto.finances.BasePaymentDto;
import com.goapi.goapi.domain.model.finances.payment.Payment;
import com.goapi.goapi.domain.model.finances.payment.paymentStatus.PaymentStatusType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class PaymentService {

    public List<BasePaymentDto> convertPaymentsToDtos(Set<? extends Payment> toAppServicePayments) {
        List<BasePaymentDto> paymentDtos = toAppServicePayments
            .stream()
            .map(payment -> {
                Integer paymentId = payment.getId();
                Date paymentDate = payment.getDate();
                BigDecimal paymentSum = payment.getSum();
                String paymentDescription = payment.getDescription();
                PaymentStatusType paymentStatusType = payment.getStatus().getPaymentStatusType();
                BasePaymentDto appServiceBillPaymentDto =
                    new BasePaymentDto(
                        paymentId,
                        paymentDate,
                        paymentStatusType,
                        paymentSum,
                        paymentDescription
                    );
                return appServiceBillPaymentDto;
            }).collect(Collectors.toList());
        return paymentDtos;
    }
}

