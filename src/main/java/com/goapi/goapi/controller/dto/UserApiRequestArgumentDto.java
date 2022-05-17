package com.goapi.goapi.controller.dto;

import com.goapi.goapi.domain.api.request.RequestArgumentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public record UserApiRequestArgumentDto(Long id, @NotBlank String argName,
                                        @NotNull RequestArgumentType requestArgumentType) implements Serializable {
    }