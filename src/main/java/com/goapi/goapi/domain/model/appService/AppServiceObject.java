package com.goapi.goapi.domain.model.appService;

import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class AppServiceObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @NotNull(message = "creation time can't be null!")
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "bill_id")
    private AppServiceBill appServiceBill;

    public AppServiceObject() {
    }

    public AppServiceObject(User owner, AppServiceBill appServiceBill) {
        this.createdAt = new Date();
        this.owner = owner;
        this.appServiceBill = appServiceBill;
    }
}