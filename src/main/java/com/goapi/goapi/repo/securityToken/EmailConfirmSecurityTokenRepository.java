package com.goapi.goapi.repo.securityToken;

import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailConfirmSecurityTokenRepository extends JpaRepository<EmailSecurityToken, Integer> {

    Optional<EmailSecurityToken> findByToken(String token);

}