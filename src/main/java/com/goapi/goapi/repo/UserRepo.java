package com.goapi.goapi.repo;

import com.goapi.goapi.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findUserByUsernameOrEmail(String username, String email);

}
