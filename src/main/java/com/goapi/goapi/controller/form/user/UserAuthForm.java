package com.goapi.goapi.controller.form.user;

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
public class UserAuthForm {

    @NotBlank(message = "dbUsername or email can't be empty!")
    private String usernameOrEmail = "";
    @NotBlank(message = "password can't be empty!")
    private String password = "";
}
