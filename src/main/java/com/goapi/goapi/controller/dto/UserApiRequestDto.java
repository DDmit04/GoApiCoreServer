package com.goapi.goapi.controller.dto;

import org.springframework.http.HttpMethod;

import javax.persistence.criteria.CriteriaBuilder;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
public record UserApiRequestDto(Integer id, @NotBlank String requestName, @NotBlank String requestTemplate, HttpMethod httpMethod) implements Serializable {
}
