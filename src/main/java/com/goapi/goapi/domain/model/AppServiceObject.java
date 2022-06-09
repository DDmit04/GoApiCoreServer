package com.goapi.goapi.domain.model;

import com.goapi.goapi.domain.model.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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
    private Integer id;

    @NotNull(message = "creation time can't be null!")
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne(orphanRemoval = false)
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