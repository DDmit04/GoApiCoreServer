package com.goapi.goapi.domain.model.appService.tariff;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.validation.constraints.PositiveOrZero;

/**
 * @author Daniil Dmitrochenkov
 **/
@Entity
@Getter
@Setter
public class DatabaseTariff extends Tariff {
    @PositiveOrZero(message = "db size must be more than 0!")
    @Column(nullable = false)
    private long maxSizeBytes;

    @Override
    public String toString() {
        return "(DatabaseTariff{" +
            "maxSizeBytes=" + maxSizeBytes +
            '}' + super.toString() + ')';
    }
}
