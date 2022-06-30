package com.goapi.goapi.domain.model.appService.userApi.request;

import lombok.Getter;
import lombok.Setter;

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
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_api_request_id")
    private UserApiRequest userApiRequest;


    public UserApiRequestArgument(String argName, RequestArgumentType requestArgumentType, UserApiRequest userApiRequest) {
        this.argName = argName;
        this.requestArgumentType = requestArgumentType;
        this.userApiRequest = userApiRequest;
    }

    public UserApiRequestArgument() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserApiRequestArgument)) return false;

        UserApiRequestArgument that = (UserApiRequestArgument) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) return false;
        if (!getArgName().equals(that.getArgName())) return false;
        return getRequestArgumentType() == that.getRequestArgumentType();
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + getArgName().hashCode();
        result = 31 * result + getRequestArgumentType().hashCode();
        return result;
    }
}