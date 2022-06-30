package com.goapi.goapi.domain.model.appService.userApi;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import com.goapi.goapi.domain.model.appService.database.Database;
import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.appService.tariff.UserApiTariff;
import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "UserApi.owner",
        attributeNodes = {
            @NamedAttributeNode("owner")
        }
    ),
    @NamedEntityGraph(
        name = "UserApi.owner.tariff",
        attributeNodes = {
            @NamedAttributeNode("owner"),
            @NamedAttributeNode("appServiceTariff")
        }
    ),
    @NamedEntityGraph(
        name = "UserApi.owner.requests",
        attributeNodes = {
            @NamedAttributeNode("owner"),
            @NamedAttributeNode("userApiRequests")
        }
    ),
    @NamedEntityGraph(
        name = "UserApi.bill.tariff",
        attributeNodes = {
            @NamedAttributeNode("appServiceBill"),
            @NamedAttributeNode("appServiceTariff")
        }
    ),
    @NamedEntityGraph(
        name = "UserApi.tariff",
        attributeNodes = {
            @NamedAttributeNode("appServiceTariff")
        }
    ),
})
public class UserApi extends AppServiceObject {

    @NotBlank(message = "user api key can't be blank!")
    @Column(nullable = false, name = "api_key")
    private String apiKey;

    @Column(nullable = false, name = "is_protected")
    private boolean isProtected;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "database_id")
    private Database database;

    @OneToMany(mappedBy = "userApi", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private Set<UserApiRequest> userApiRequests = new LinkedHashSet<>();

    public UserApi(String apiKey, boolean isProtected, Database database, UserApiTariff userApiTariff, String userApiName, User owner, AppServiceBill appServiceBill) {
        super(owner, appServiceBill, userApiName, userApiTariff);
        this.apiKey = apiKey;
        this.isProtected = isProtected;
        this.database = database;
    }

    public UserApi() {
    }

    @Override
    public UserApiTariff getAppServiceTariff() {
        Tariff appServiceTariff = super.getAppServiceTariff();
        return (UserApiTariff) appServiceTariff;
    }

    @Override
    public String toString() {
        return "(UserApi{" +
            "isProtected=" + isProtected +
            '}' + super.toString() + ')';
    }
}