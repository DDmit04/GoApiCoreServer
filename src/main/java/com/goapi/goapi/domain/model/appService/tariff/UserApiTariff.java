package com.goapi.goapi.domain.model.appService.tariff;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.Positive;

@Entity
@Getter
@Setter
public class UserApiTariff extends Tariff {
    @Positive(message = "user api tariff max requests count must be positive!")
    @Column(nullable = false, name = "requests_count")
    @Access(AccessType.PROPERTY)
    private int maxRequestsCount;

    @Override
    public String toString() {
        return "(UserApiTariff{" +
            "maxRequestsCount=" + maxRequestsCount +
            '}' + super.toString() + ')';
    }
}