package com.goapi.goapi.repo.api;

import com.goapi.goapi.domain.api.request.UserApiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestRepository extends JpaRepository<UserApiRequest, Integer> {
}