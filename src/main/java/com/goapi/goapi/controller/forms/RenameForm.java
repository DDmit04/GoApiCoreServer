package com.goapi.goapi.controller.forms;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class RenameForm {

    @NotBlank(message = "name can't be blank!")
    private String name;

    public RenameForm(String name) {
        this.name = name;
    }

    public RenameForm() {
    }
}
