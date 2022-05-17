package com.goapi.goapi.repo.api;

import com.goapi.goapi.domain.api.request.UserApiRequestArgument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestArgumentRepository extends JpaRepository<UserApiRequestArgument, Integer> {
}