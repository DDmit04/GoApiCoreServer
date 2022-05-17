package com.goapi.goapi.repo.database;

import com.goapi.goapi.domain.database.DatabaseTariff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DbatabaseTariffRepo extends JpaRepository<DatabaseTariff, Integer> {

}
