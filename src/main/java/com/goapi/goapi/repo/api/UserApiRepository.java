package com.goapi.goapi.repo.api;

import com.goapi.goapi.domain.api.UserApi;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserApiRepository extends JpaRepository<UserApi, Integer> {
}