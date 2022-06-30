package com.goapi.goapi.repo.appService.database;

import com.goapi.goapi.domain.model.appService.database.Database;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DatabaseRepo extends JpaRepository<Database, Integer> {

    String selectDatabaseByIdQuery = "from Database d where d.id = :databaseId";

    List<Database> findAllDatabasesByOwnerId(Integer ownerId);

    @Query(selectDatabaseByIdQuery)
    @EntityGraph("Database.owner")
    Optional<Database> findDatabaseByIdWithOwner(@Param("databaseId") Integer databaseId);

    int countByOwner_Id(Integer userId);

    @Query(selectDatabaseByIdQuery)
    @EntityGraph("Database.tariff")
    Optional<Database> findByIdWithTariff(@Param("databaseId") Integer databaseId);

}
