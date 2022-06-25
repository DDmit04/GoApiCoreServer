package com.goapi.goapi.domain.model.appService.tariff;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

/**
 * @author Daniil Dmitrochenkov
 **/
@Entity
@Getter
@Setter
@Table
@Inheritance(strategy = InheritanceType.JOINED)
public class Tariff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotBlank(message = "tariff name can't be blank!")
    @Column(nullable = false)
    private String tariff_name;
    @NotNull(message = "tariff cost can't be null!")
    @Positive(message = "tariff cost must be positive!")
    @Column(nullable = false, columnDefinition = "decimal default 0")
    @Access(AccessType.PROPERTY)
    private BigDecimal costPerDay;
}
