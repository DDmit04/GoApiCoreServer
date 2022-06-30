package com.goapi.goapi.repo.appService;

import com.goapi.goapi.domain.model.appService.AppServiceObject;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public interface AppServiceObjectRepository<T extends AppServiceObject, E extends Serializable> extends JpaRepository<T, E> {

    String selectAppServiceObjectByIdQuery = "from AppServiceObject where id = :appServiceId";
    @Query(selectAppServiceObjectByIdQuery)
    @EntityGraph("AppServiceObject.bill.tariff")
    Optional<AppServiceObject> findAppServiceObjectByIdWithTariffAndBill(@Param("appServiceId") Integer appServiceId);

    @Query(selectAppServiceObjectByIdQuery)
    Optional<AppServiceObject> findAppServiceObjectById(@Param("appServiceId")Integer appServiceId);

    @Query(selectAppServiceObjectByIdQuery)
    @EntityGraph("AppServiceObject.owner")
    Optional<AppServiceObject> findAppServiceObjectByIdWithOwner(@Param("appServiceId")Integer appServiceId);

    @Query(selectAppServiceObjectByIdQuery)
    @EntityGraph("AppServiceObject.owner.bill")
    Optional<AppServiceObject> findAppServiceObjectByIdWithOwnerAndBill(@Param("appServiceId")Integer appServiceId);
    @EntityGraph("AppServiceObject.bill.tariff")
    List<T> findAll();
}
