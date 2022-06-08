package com.goapi.goapi.repo;

import com.goapi.goapi.domain.model.token.SecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SecurityTokenRepository extends JpaRepository<SecurityToken, Integer> {

    Optional<SecurityToken> findByToken(String token);

}