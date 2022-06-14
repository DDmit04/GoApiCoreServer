package com.goapi.goapi.domain.model.appService.tariff;

import com.goapi.goapi.domain.model.appService.database.Database;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @OneToMany(mappedBy = "dbTariff", fetch = FetchType.LAZY)
    private Set<Database> databases = new LinkedHashSet<>();
}
