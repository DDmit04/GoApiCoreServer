package com.goapi.goapi.domain.model.appService;

import com.goapi.goapi.domain.model.appService.tariff.Tariff;
import com.goapi.goapi.domain.model.finances.bill.AppServiceBill;
import com.goapi.goapi.domain.model.user.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Entity
@NamedEntityGraphs({
    @NamedEntityGraph(
        name = "AppServiceObject.bill.tariff",
        attributeNodes = {
            @NamedAttributeNode("appServiceTariff"),
            @NamedAttributeNode("appServiceBill")
        }
    ),
    @NamedEntityGraph(
        name = "AppServiceObject.owner",
        attributeNodes = {
            @NamedAttributeNode("owner"),
        }
    )
})
public class AppServiceObject {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Access(AccessType.PROPERTY)
    private Integer id;

    @NotBlank(message = "db name can't be blank!")
    @Column(nullable = false)
    private String name;

    @NotNull(message = "creation time can't be null!")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date createdAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "user_id")
    private User owner;

    @OneToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "bill_id")
    private AppServiceBill appServiceBill;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "tariff_id")
    private Tariff appServiceTariff;

    @Embedded
    private AppServiceObjectStatus appServiceObjectStatus;

    public AppServiceObject() {
    }

    public AppServiceObject(User owner, AppServiceBill appServiceBill, String name, Tariff tariff) {
        this.createdAt = new Date();
        this.owner = owner;
        this.name = name;
        this.appServiceBill = appServiceBill;
        this.appServiceTariff = tariff;
    }
}