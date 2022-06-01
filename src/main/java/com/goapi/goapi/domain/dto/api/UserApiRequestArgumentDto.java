package com.goapi.goapi.domain.dto.api;

import com.goapi.goapi.domain.model.userApi.request.RequestArgumentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public record UserApiRequestArgumentDto(Long id, @NotBlank String argName,
                                        @NotNull RequestArgumentType requestArgumentType) implements Serializable {
    }