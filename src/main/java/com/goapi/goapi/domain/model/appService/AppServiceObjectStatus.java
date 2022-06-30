package com.goapi.goapi.domain.model.appService;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Embeddable
@Getter
@Setter
public class AppServiceObjectStatus {

    @NotNull(message = "app service object status can't be null!")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    @Access(AccessType.PROPERTY)
    private AppServiceStatusType status;

    @NotNull(message = "app service object status date can't be null!")
    @Column(nullable = false, name = "status_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date statusDate;

    public AppServiceObjectStatus() {
        this.status = AppServiceStatusType.DISABLED;
        this.statusDate = new Date();
    }

    @Override
    public String toString() {
        return "AppServiceObjectStatus{" +
            "status=" + status +
            ", statusDate=" + statusDate +
            '}';
    }
}