package com.goapi.goapi.domain.model.appService.database;

import com.example.DatabaseType;
import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnTransformer;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Daniil Dmitrochenkov
 **/
@Entity
@Getter
@Setter
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "Database.owner",
        attributeNodes = {
            @NamedAttributeNode("owner")
        }
    ),
    @NamedEntityGraph(
        name = "Database.tariff",
        attributeNodes = {
            @NamedAttributeNode("appServiceTariff")
        }
    ),
})
public class Database extends AppServiceObject {
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
    @Column(nullable = false, columnDefinition = "bytea")
    private String databasePassword;
    @NotNull(message = "db type can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DatabaseType databaseType;
    @Column(nullable = false, name = "accept_external_connections")
    private boolean acceptExternalConnections;

    public Database(User owner, AppServiceBill appServiceBill, String databaseName, String databasePassword, DatabaseType databaseType, DatabaseTariff dbTariff) {
        super(owner, appServiceBill, databaseName, dbTariff);
        this.databasePassword = databasePassword;
        this.databaseType = databaseType;
        this.acceptExternalConnections = true;
    }

    public Database() {
    }

    @Override
    public DatabaseTariff getAppServiceTariff() {
        Tariff appServiceTariff = super.getAppServiceTariff();
        return (DatabaseTariff) appServiceTariff;
    }

    @Override
    public String toString() {
        return "(Database{" +
            "databaseType=" + databaseType +
            ", acceptExternalConnections=" + acceptExternalConnections +
            '}' + super.toString() + ')';
    }
}
