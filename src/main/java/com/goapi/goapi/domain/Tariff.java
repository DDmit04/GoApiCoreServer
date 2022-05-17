package com.goapi.goapi.domain;

import com.fasterxml.jackson.annotation.JsonView;
import com.goapi.goapi.views.CommonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@MappedSuperclass
@Getter
@Setter
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CommonView.Id.class)
    private Integer id;
    @Column(nullable = false)
    @JsonView(CommonView.CoreData.class)
    private String name;
    @Column(columnDefinition = "decimal default 0")
    @JsonView(CommonView.CoreData.class)
    private BigDecimal costPerMonth;
}
