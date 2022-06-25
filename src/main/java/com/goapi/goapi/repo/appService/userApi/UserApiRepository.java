package com.goapi.goapi.repo.appService.userApi;

import com.goapi.goapi.domain.model.appService.userApi.UserApi;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Integer> {

    String selectApiByIdQuery = "from UserApi ua where ua.id = :id";

    List<UserApi> findByOwner_id(Integer apiOwnerId);

    @Query("select count(uar) " +
        "from UserApiRequest uar " +
        "where uar.userApi.id = :apiId")
    int getTotalUserApiRequestsCount(@Param(value = "apiId") Integer apiId);

    @Query(selectApiByIdQuery)
    @EntityGraph("UserApi.owner")
    Optional<UserApi> findUserApiByIdWithOwner(@Param("id") Integer apiId);

    @Query(selectApiByIdQuery)
    @EntityGraph("UserApi.owner.requests")
    Optional<UserApi> findUserApiByIdWithOwnerAndRequests(@Param("id") Integer apiId);

    @Query(selectApiByIdQuery)
    @EntityGraph("UserApi.bill.tariff")
    Optional<UserApi> findUserApiByIdWithBillAndTariff(@Param("id") Integer apiId);

    @Query("select count(ua) " +
        "from UserApi ua " +
        "where ua.owner.id = :userId")
    int getTotalUserApisCount(@Param("userId") Integer userId);

    @Query(selectApiByIdQuery)
    @EntityGraph("UserApi.owner.tariff")
    Optional<UserApi> findUserApiByIdWithTariffAndOwner(@Param("id")Integer userApiTariffId);
}