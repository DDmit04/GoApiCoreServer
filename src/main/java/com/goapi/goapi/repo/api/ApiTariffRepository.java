package com.goapi.goapi.repo.api;

import com.goapi.goapi.domain.api.UserApiTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApiTariffRepository extends JpaRepository<UserApiTariff, Integer> {
}