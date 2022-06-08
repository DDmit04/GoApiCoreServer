package com.goapi.goapi.repo;

import com.goapi.goapi.domain.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByJwtRefreshToken(String token);

    Optional<User> findUserByUsernameOrEmail(String username, String email);

    Optional<User> findUserByEmail(String newEmail);
}
