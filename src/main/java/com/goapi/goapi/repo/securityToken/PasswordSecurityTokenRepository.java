package com.goapi.goapi.repo.securityToken;

import com.goapi.goapi.domain.model.user.token.PasswordSecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordSecurityTokenRepository extends JpaRepository<PasswordSecurityToken, Integer> {
    Optional<PasswordSecurityToken> findByToken(String token);

}