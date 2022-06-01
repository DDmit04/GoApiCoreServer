package com.goapi.goapi.repo.userApi;

import com.goapi.goapi.domain.model.userApi.request.UserApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestRepository extends JpaRepository<UserApiRequest, Integer> {

}