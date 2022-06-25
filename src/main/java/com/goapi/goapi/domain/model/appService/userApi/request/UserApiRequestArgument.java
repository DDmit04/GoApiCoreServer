package com.goapi.goapi.domain.model.appService.userApi.request;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

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
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
public class UserApiRequestArgument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "request argument name can't be blank!")
    @Column(nullable = false, name = "arg_name")
    private String argName;

    @NotNull(message = "request argument type can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "request_argument_type")
    private RequestArgumentType requestArgumentType;

    @ManyToOne(optional = false)
    @JoinColumn(nullable = false, name = "api_request_id")
    private UserApiRequest userApiRequest;

    public UserApiRequestArgument(String argName, RequestArgumentType requestArgumentType) {
        this.argName = argName;
        this.requestArgumentType = requestArgumentType;
    }

    public UserApiRequestArgument() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserApiRequestArgument that = (UserApiRequestArgument) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}