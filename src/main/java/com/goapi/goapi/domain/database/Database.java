package com.goapi.goapi.domain.database;

import com.example.DatabaseType;
import com.fasterxml.jackson.annotation.JsonView;
import com.goapi.goapi.domain.user.User;
import com.goapi.goapi.views.CommonView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Daniil Dmitrochenkov
 **/
@Table
@Entity
@Getter
@Setter
public class Database {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(CommonView.Id.class)
    private Integer id;
    @NotBlank(message = "db name can't be blank!")
    @JsonView(CommonView.CoreData.class)
    private String name;
    @NotBlank(message = "db password can't be blank!")
    @JsonView(CommonView.FullData.class)
    private String username;
    @NotNull(message = "db creation time can't be null!")
    @JsonView(CommonView.CoreData.class)
    private Date createdAt;
    @NotBlank(message = "db password can't be blank!")
    @JsonView(CommonView.FullData.class)
    private String password;
    @Column
    @JsonView(CommonView.CoreData.class)
    private BigDecimal moneyAmount = BigDecimal.valueOf(0);
    @Enumerated(EnumType.STRING)
    @JsonView(CommonView.CoreData.class)
    private DatabaseType databaseType;
    @ManyToOne
    @JoinColumn(name = "tariff_id")
    @JsonView(CommonView.FullData.class)
    private DatabaseTariff dbTariff;
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonView(CommonView.FullData.class)
    private User owner;

    public Database(String name, String username, Date createdAt, String password, BigDecimal moneyAmount, DatabaseType databaseType, DatabaseTariff dbTariff, User owner) {
        this.name = name;
        this.username = username;
        this.createdAt = createdAt;
        this.password = password;
        this.moneyAmount = moneyAmount;
        this.databaseType = databaseType;
        this.dbTariff = dbTariff;
        this.owner = owner;
    }

    public Database() {
    }
}
