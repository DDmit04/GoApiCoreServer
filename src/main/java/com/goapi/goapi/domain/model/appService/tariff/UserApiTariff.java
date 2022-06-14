package com.goapi.goapi.domain.model.appService.tariff;

import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Table
public class UserApiTariff extends Tariff {
    @PositiveOrZero
    @Column(name = "requests_count")
    private Integer maxRequestsCount;

    @OneToMany(mappedBy = "userApiTariff", orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<UserApi> userApis = new LinkedHashSet<>();

}