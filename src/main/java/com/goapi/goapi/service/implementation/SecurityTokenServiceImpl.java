package com.goapi.goapi.service.implementation;

import com.goapi.goapi.domain.model.token.SecurityToken;
import com.goapi.goapi.domain.model.token.TokenType;
import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.repo.SecurityTokenRepository;
import com.goapi.goapi.service.interfaces.SecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Daniil Dmitrochenkov
 **/
@Service
@RequiredArgsConstructor
public class SecurityTokenServiceImpl implements SecurityTokenService {

    private final SecurityTokenRepository securityTokenRepository;

    private final PasswordEncoder passwordEncoder;
    @Value("${tokens.life.password}")
    private int passwordResetTokenLifetime;

    @Value("${tokens.life.email}")
    private int emailConfirmTokenLifetime;

    @Value("${tokens.life.email-change}")
    private int emailChangeTokenLifetime;

    @Override
    public SecurityToken createNewPasswordResetToken(User user) {
        SecurityToken securityToken = createToken(user, passwordResetTokenLifetime, TokenType.PASSWORD_RESET);
        return securityToken;
    }

    @Override
    public SecurityToken createNewEmailConfirmToken(User user) {
        SecurityToken securityToken = createToken(user, emailConfirmTokenLifetime, TokenType.EMAIL_CONFIRM);
        return securityToken;
    }

    @Override
    public SecurityToken createNewEmailChangeCode(User user) {
        SecurityToken securityToken = createToken(user, emailChangeTokenLifetime, TokenType.EMAIL_CHANGE);
        return securityToken;
    }

    @Override
    public SecurityToken findEmailChangeToken(String tokenString) {
        String encodedToken = passwordEncoder.encode(tokenString);
        return findValidToken(encodedToken, TokenType.EMAIL_CHANGE);
    }

    @Override
    public SecurityToken findEmailConfirmToken(String tokenString) {
        return findValidToken(tokenString, TokenType.EMAIL_CONFIRM);
    }

    @Override
    public SecurityToken findPasswordResetToken(String tokenString) {
        return findValidToken(tokenString, TokenType.PASSWORD_RESET);
    }

    @Override
    public void invalidateToken(SecurityToken token) {
        token.setValid(false);
        securityTokenRepository.save(token);
    }

    private SecurityToken createToken(User owner, int lifetime, TokenType tokenType) {
        UUID newUuid = UUID.randomUUID();
        String uuidString = newUuid.toString();
        Date expireDate = Timestamp.valueOf(LocalDateTime.now().plusSeconds(lifetime));
        SecurityToken securityToken = new SecurityToken(uuidString, expireDate, tokenType, owner);
        securityToken = securityTokenRepository.save(securityToken);
        return securityToken;
    }

    private SecurityToken findValidToken(String tokenString, TokenType targetTokenType) {
        Optional<SecurityToken> token = securityTokenRepository.findByToken(tokenString);
        return token.map(tok -> {
            TokenType tokenType = tok.getTokenType();
            boolean tokenTypeMatch = tokenType.equals(targetTokenType);
            boolean tokenValid = tok.isValid();
            boolean tokenAccepted = tokenValid && tokenTypeMatch;
            if (tokenAccepted) {
                return tok;
            }
            throw new RuntimeException();
        }).orElseThrow(() -> new RuntimeException());
    }
}
