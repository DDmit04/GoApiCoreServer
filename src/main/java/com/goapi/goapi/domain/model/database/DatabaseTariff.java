package com.goapi.goapi.domain.model.database;

import com.goapi.goapi.domain.model.Tariff;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.PositiveOrZero;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
@Table
@Entity
@Getter
@Setter
public class DatabaseTariff extends Tariff {
    @PositiveOrZero(message = "db size must be more than 0!")
    private long maxSizeBytes;
    @OneToMany(mappedBy = "dbTariff")
    private Set<Database> databases = new LinkedHashSet<>();
}
