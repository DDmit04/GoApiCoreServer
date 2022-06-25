package com.goapi.goapi.domain.model.appService.userApi.request;

import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.springframework.http.HttpMethod;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "UserApiRequest.arguments",
        attributeNodes = {
            @NamedAttributeNode("userApiRequestArguments"),
        }
    )
})
public class UserApiRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "user_api_id")
    private UserApi userApi;
    @NotBlank(message = "request name can't be blank!")
    @Column(nullable = false, name = "request_name")
    private String requestName;

    @NotBlank(message = "request template can't be blank!")
    @Column(nullable = false, name = "request_template")
    private String requestTemplate;

    @NotNull(message = "request http method can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "http_method")
    private HttpMethod httpMethod;

    @OneToMany(mappedBy = "userApiRequest", orphanRemoval = true, cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private Set<UserApiRequestArgument> userApiRequestArguments = new LinkedHashSet<>();

    public UserApiRequest(UserApi userApi, String requestName, String requestTemplate, HttpMethod httpMethod, Set<UserApiRequestArgument> userApiRequestArguments) {
        this.userApi = userApi;
        this.requestName = requestName;
        this.requestTemplate = requestTemplate;
        this.httpMethod = httpMethod;
        this.userApiRequestArguments = userApiRequestArguments;
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