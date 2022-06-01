package com.goapi.goapi.domain.model.userApi.request;

import com.goapi.goapi.domain.model.userApi.UserApi;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.http.HttpMethod;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class UserApiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "user_api_id")
    private UserApi userApi;
    @NotBlank
    @Column(name = "request_name")
    private String requestName;

    @NotBlank
    @Column(name = "request_template")
    private String requestTemplate;

    @Enumerated(EnumType.STRING)
    @Column(name = "http_method")
    private HttpMethod httpMethod;

    @OneToMany(mappedBy = "userApiRequest", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private Set<UserApiRequestArgument> userApiRequestArguments = new LinkedHashSet<>();

    public UserApiRequest(UserApi userApi, String requestName, String requestTemplate, HttpMethod httpMethod) {
        this.userApi = userApi;
        this.requestName = requestName;
        this.requestTemplate = requestTemplate;
        this.httpMethod = httpMethod;
    }

    public UserApiRequest() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserApiRequest that = (UserApiRequest) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}