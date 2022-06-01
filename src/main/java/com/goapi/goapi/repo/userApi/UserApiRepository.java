package com.goapi.goapi.repo.userApi;

import com.goapi.goapi.domain.model.userApi.UserApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Integer> {

    List<UserApi> findByOwner_id(Integer apiOwnerId);

    @Query(
        nativeQuery = true,
        value = "" +
            "select count(uar) " +
            "from user_api ua join user_api_request uar on ua.id = uar.user_api_id" +
            " where ua.user_id = :userId")
    int getTotalUserApiRequestsCount(@Param(value = "userId") Integer userId);

}