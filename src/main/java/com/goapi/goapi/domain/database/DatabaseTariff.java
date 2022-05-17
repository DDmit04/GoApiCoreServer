package com.goapi.goapi.domain.database;

import com.fasterxml.jackson.annotation.JsonView;
import com.goapi.goapi.domain.Tariff;
import com.goapi.goapi.views.CommonView;
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
    @JsonView(CommonView.CoreData.class)
    private long maxSizeBytes;
    @OneToMany(mappedBy = "dbTariff")
    @JsonView(CommonView.FullData.class)
    private Set<Database> databases = new LinkedHashSet<>();
}
