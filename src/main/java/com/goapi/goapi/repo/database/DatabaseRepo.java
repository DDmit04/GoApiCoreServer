package com.goapi.goapi.repo.database;

import com.goapi.goapi.domain.database.Database;
import com.goapi.goapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseRepo extends JpaRepository<Database, Integer> {

    List<Database> findAllByOwnerId(Long ownerId);
}
