package com.goapi.goapi.repo.appService.userApi;

import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiRequestRepository extends JpaRepository<UserApiRequest, Integer> {

    Optional<UserApiRequest> findByIdAndUserApi_Id(Integer requestId, Integer userApiId);

    @Query(nativeQuery = true, value = "select * from user_api_request_argument where id = :requestId and api_request_id = :apiId")
    @EntityGraph("UserApiRequest.arguments")
    Optional<UserApiRequest> findByIdAndUserApiIdWithArguments(@Param("requestId") Integer requestId, @Param("apiId") Integer apiId);
}