package com.goapi.goapi.repo.userApi;

import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApiRequestRepository extends JpaRepository<UserApiRequest, Integer> {

    Optional<UserApiRequest> findByIdAndUserApi_Id(Integer requestId, Integer userApiId);
}