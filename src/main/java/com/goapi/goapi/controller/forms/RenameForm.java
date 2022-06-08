package com.goapi.goapi.controller.forms;

import lombok.Getter;

/**
 * @author Daniil Dmitrochenkov
 **/
@Getter
public class RenameForm {

    private String name;

    public RenameForm(String name) {
        this.name = name;
    }

    public RenameForm() {
    }
}
