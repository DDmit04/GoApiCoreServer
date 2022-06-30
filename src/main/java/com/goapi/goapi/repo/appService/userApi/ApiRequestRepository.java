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

    @Query("from UserApiRequest req where req.id = :requestId")
    @EntityGraph("UserApiRequest.arguments")
    Optional<UserApiRequest> findByIdWithArguments(@Param("requestId") Integer requestId);
}