package com.goapi.goapi.controller.forms.user;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class UserRegForm {

    @NotBlank(message = "dbUsername can't be empty!")
    private String username = "";
    @NotBlank(message = "email can't be empty!")
    private String email = "";
    @NotBlank(message = "password can't be empty!")
    private String password = "";
}
