package com.goapi.goapi.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class UserDto implements Serializable {
    private final Integer id;
    @NotBlank(message = "username can't be blank!")
    private final String username;
    @NotBlank(message = "email can't be blank!")
    private final String email;
    private final BigDecimal moneyAmount;
}
