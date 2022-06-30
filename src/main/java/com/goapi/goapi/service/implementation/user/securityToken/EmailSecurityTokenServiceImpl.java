package com.goapi.goapi.service.implementation.user.securityToken;

import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.EmailSecurityToken;
import com.goapi.goapi.exception.securityToken.TokenNotAcceptedException;
import com.goapi.goapi.exception.securityToken.TokenNotFoundException;
import com.goapi.goapi.repo.securityToken.EmailConfirmSecurityTokenRepository;
import com.goapi.goapi.service.interfaces.user.securityToken.EmailSecurityTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
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
public class EmailSecurityTokenServiceImpl implements EmailSecurityTokenService {

    private final EmailConfirmSecurityTokenRepository emailConfirmSecurityTokenRepository;

    @Value("${tokens.life.email}")
    private int emailConfirmTokenLifetime;

    @Value("${tokens.life.email-change}")
    private int emailChangeTokenLifetime;


    @Override
    public EmailSecurityToken createNewEmailConfirmToken(User user) {
        EmailSecurityToken securityToken = createEmailConfirmToken(user, emailConfirmTokenLifetime);
        return securityToken;
    }


    @Override
    public EmailSecurityToken createNewEmailChangeToken(User user, String email) {
        EmailSecurityToken securityToken = createEmailConfirmToken(user, email, emailChangeTokenLifetime);
        return securityToken;
    }


    @Override
    public EmailSecurityToken findEmailToken(String tokenString) {
        Optional<EmailSecurityToken> token = emailConfirmSecurityTokenRepository.findByToken(tokenString);
        return token.map(tok -> {
            Integer tokenId = tok.getId();
            boolean tokenValid = tok.isIsValid();
            if (tokenValid) {
                return tok;
            }
            throw new TokenNotAcceptedException(tokenId);
        }).orElseThrow(() -> new TokenNotFoundException(tokenString));
    }


    @Override
    public void invalidateEmailToken(EmailSecurityToken token) {
        token.setValid(false);
        emailConfirmSecurityTokenRepository.save(token);
    }

    private EmailSecurityToken createEmailConfirmToken(User user, String userEmail, int lifetime) {
        UUID newUuid = UUID.randomUUID();
        String uuidString = newUuid.toString();
        Date expireDate = Timestamp.valueOf(LocalDateTime.now().plusSeconds(lifetime));
        EmailSecurityToken securityToken = new EmailSecurityToken(uuidString, expireDate, user, userEmail);
        securityToken = emailConfirmSecurityTokenRepository.save(securityToken);
        return securityToken;
    }

    private EmailSecurityToken createEmailConfirmToken(User user, int lifetime) {
        String userEmail = user.getEmail();
        return createEmailConfirmToken(user, userEmail, lifetime);
    }

}
