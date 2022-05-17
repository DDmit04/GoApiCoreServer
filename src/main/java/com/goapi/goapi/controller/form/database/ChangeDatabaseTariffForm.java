package com.goapi.goapi.controller.form.database;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class ChangeDatabaseTariffForm {

    @NotNull(message = "tariffId can't be null!")
    @Positive(message = "tariffId must be positive integer!")
    private Integer tariffId;

    public ChangeDatabaseTariffForm(Integer tariffId) {
        this.tariffId = tariffId;
    }

    public ChangeDatabaseTariffForm() {
    }
}
