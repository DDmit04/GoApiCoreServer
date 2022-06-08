package com.goapi.goapi.domain.model;

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
public abstract class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String tariff_name;
    @Column(columnDefinition = "decimal default 0")
    private BigDecimal costPerMonth;
}
