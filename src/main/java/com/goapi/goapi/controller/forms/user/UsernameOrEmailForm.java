package com.goapi.goapi.controller.forms.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UsernameOrEmailForm {

    @NotBlank(message = "dbUsername or email can't be empty!")
    private String usernameOrEmail = "";
}
