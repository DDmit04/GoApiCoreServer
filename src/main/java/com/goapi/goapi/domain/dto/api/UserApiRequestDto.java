package com.goapi.goapi.domain.dto.api;

import lombok.Data;
import org.springframework.http.HttpMethod;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public final class UserApiRequestDto implements Serializable {
    private final Integer id;
    private final @NotBlank String requestName;
    private final @NotBlank String requestTemplate;
    private final HttpMethod httpMethod;
    private final String requestUrl;

}
