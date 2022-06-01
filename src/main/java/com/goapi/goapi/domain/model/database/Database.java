package com.goapi.goapi.domain.model.database;

import com.example.DatabaseType;
import com.goapi.goapi.domain.model.Payment;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

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
    private Integer id;
    @NotBlank(message = "db name can't be blank!")
    private String name;
    @NotNull(message = "db creation time can't be null!")
    private Date createdAt;

    @ColumnTransformer(
        read = """
            pgp_sym_decrypt(
                password,
                current_setting('encrypt.key')
            )
            """,
        write = """
            pgp_sym_encrypt(
                ?,
                current_setting('encrypt.key')
            )
            """
    )
    @NotBlank(message = "db password can't be blank!")
    @Column(columnDefinition = "bytea")
    private String password;
    @Column
    private BigDecimal moneyAmount = BigDecimal.valueOf(0);
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;
    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private DatabaseTariff dbTariff;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany
    @JoinTable(name = "database_payments",
        joinColumns = @JoinColumn(name = "database_id"),
        inverseJoinColumns = @JoinColumn(name = "payments_id"))
    private Set<Payment> payments = new LinkedHashSet<>();

    public Database(String name, Date createdAt, String password, BigDecimal moneyAmount, DatabaseType databaseType, DatabaseTariff dbTariff, User owner) {
        this.name = name;
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
