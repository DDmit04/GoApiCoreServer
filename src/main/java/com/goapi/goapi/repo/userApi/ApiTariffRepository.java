package com.goapi.goapi.repo.userApi;

import com.goapi.goapi.domain.model.userApi.UserApiTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiTariffRepository extends JpaRepository<UserApiTariff, Integer> {
}