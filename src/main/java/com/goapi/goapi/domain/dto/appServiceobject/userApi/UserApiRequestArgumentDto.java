package com.goapi.goapi.domain.dto.appServiceobject.userApi;

import com.goapi.goapi.domain.model.appService.userApi.request.RequestArgumentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public record UserApiRequestArgumentDto(Integer id, @NotBlank String argName,
                                        @NotNull RequestArgumentType requestArgumentType) implements Serializable {
}