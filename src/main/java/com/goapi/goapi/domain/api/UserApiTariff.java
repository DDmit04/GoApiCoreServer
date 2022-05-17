package com.goapi.goapi.domain.api;

import com.goapi.goapi.domain.Tariff;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
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


    @OneToMany(mappedBy = "userApiTariff", orphanRemoval = true)
    private Set<UserApi> userApis = new LinkedHashSet<>();

}