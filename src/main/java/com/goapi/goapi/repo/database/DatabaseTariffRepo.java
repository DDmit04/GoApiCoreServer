package com.goapi.goapi.repo.database;

import com.goapi.goapi.domain.model.appService.tariff.DatabaseTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DatabaseTariffRepo extends JpaRepository<DatabaseTariff, Integer> {

}
