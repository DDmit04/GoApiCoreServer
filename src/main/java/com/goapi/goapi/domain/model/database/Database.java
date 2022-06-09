package com.goapi.goapi.domain.model.database;

import com.example.DatabaseType;
import com.goapi.goapi.domain.model.AppServiceObject;
import com.goapi.goapi.domain.model.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

/**
 * @author Daniil Dmitrochenkov
 **/
@Table
@Entity
@Getter
@Setter
public class Database extends AppServiceObject {
    @NotBlank(message = "db name can't be blank!")
    private String databaseName;
    @ColumnTransformer(
        read = """
            pgp_sym_decrypt(
                database_password,
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
    private String databasePassword;
    @Enumerated(EnumType.STRING)
    private DatabaseType databaseType;
    @ManyToOne
    @JoinColumn(name = "tariff_id")
    private DatabaseTariff dbTariff;

    public Database(User owner, AppServiceBill appServiceBill, String databaseName, String databasePassword, DatabaseType databaseType, DatabaseTariff dbTariff) {
        super(owner, appServiceBill);
        this.databaseName = databaseName;
        this.databasePassword = databasePassword;
        this.databaseType = databaseType;
        this.dbTariff = dbTariff;
    }

    public Database() {
    }
}
