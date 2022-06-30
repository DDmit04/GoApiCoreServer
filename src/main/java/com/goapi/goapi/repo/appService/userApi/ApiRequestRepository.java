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

    @Query("from UserApiRequest req where req.id = :requestId and req.userApi.id = :userApiId")
    @EntityGraph("UserApiRequest.arguments")
    Optional<UserApiRequest> findByIdAndUserApiIdWithArguments(@Param("requestId") Integer requestId, @Param("userApiId") Integer userApiId);

    @Query("from UserApiRequest req where req.id = :requestId")
    @EntityGraph("UserApiRequest.arguments")
    Optional<UserApiRequest> findByIdWithArguments(@Param("requestId") Integer requestId);
}