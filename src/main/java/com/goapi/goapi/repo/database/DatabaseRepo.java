package com.goapi.goapi.repo.database;

import com.goapi.goapi.domain.model.database.Database;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DatabaseRepo extends JpaRepository<Database, Integer> {

    List<Database> findAllByOwnerId(Integer ownerId);
}
