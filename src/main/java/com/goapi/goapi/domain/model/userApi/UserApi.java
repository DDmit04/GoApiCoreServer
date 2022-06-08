package com.goapi.goapi.domain.model.userApi;

import com.goapi.goapi.domain.model.AppServiceObject;
import com.goapi.goapi.domain.model.bill.Bill;
import com.goapi.goapi.domain.model.database.Database;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class UserApi extends AppServiceObject {

    @Column(name = "user_api_name")
    private String userApiName;
    @NotBlank
    @Column(name = "api_key")
    private String apiKey;

    @Column(name = "is_protected")
    private boolean isProtected;

    @ManyToOne
    @JoinColumn(name = "database_id")
    private Database database;

    @OneToMany(mappedBy = "userApi", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<UserApiRequest> userApiRequests = new LinkedHashSet<>();

    public UserApi(String apiKey, boolean isProtected, Database database, String userApiName, User owner, Bill bill) {
        super(owner, bill);
        this.apiKey = apiKey;
        this.isProtected = isProtected;
        this.database = database;
        this.userApiName = userApiName;
    }

    public UserApi() {
    }

}