package com.goapi.goapi.domain.dto.userApi;

import lombok.Data;

import java.io.Serializable;

/**
 * @author Daniil Dmitrochenkov
 **/
@Data
public class SummaryUserApiDto implements Serializable {
    private final Integer id;
    private final boolean isProtected;
    private final String name;
    private final int requestsCount;
}
