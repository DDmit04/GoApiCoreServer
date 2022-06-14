package com.goapi.goapi.repo.userApi;

import com.goapi.goapi.domain.model.appService.userApi.request.UserApiRequestArgument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiRequestArgumentRepository extends JpaRepository<UserApiRequestArgument, Integer> {
}