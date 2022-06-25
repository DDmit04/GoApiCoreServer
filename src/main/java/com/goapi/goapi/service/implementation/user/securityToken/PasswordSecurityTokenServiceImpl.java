package com.goapi.goapi.service.implementation.user.securityToken;

import com.goapi.goapi.domain.model.user.User;
import com.goapi.goapi.domain.model.user.token.PasswordSecurityToken;
import com.goapi.goapi.exception.securityToken.TokenNotAcceptedException;
import com.goapi.goapi.exception.securityToken.TokenNotFoundException;
import com.goapi.goapi.repo.securityToken.PasswordSecurityTokenRepository;
import com.goapi.goapi.service.interfaces.user.securityToken.PasswordSecurityTokenService;
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
public class PasswordSecurityTokenServiceImpl implements PasswordSecurityTokenService {

    private final PasswordSecurityTokenRepository passwordSecurityTokenRepository;

    @Value("${tokens.life.password}")
    private int passwordResetTokenLifetime;

    @Override
    public PasswordSecurityToken createNewPasswordResetToken(User user) {
        PasswordSecurityToken securityToken = createPasswordResetToken(user, passwordResetTokenLifetime);
        return securityToken;
    }

    @Override
    public PasswordSecurityToken findPasswordResetToken(String tokenString) {
        Optional<PasswordSecurityToken> token = passwordSecurityTokenRepository.findByToken(tokenString);
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
    public void invalidatePasswordResetToken(PasswordSecurityToken token) {
        token.setValid(false);
        passwordSecurityTokenRepository.save(token);
    }

    private PasswordSecurityToken createPasswordResetToken(User user, int lifetime) {
        UUID newUuid = UUID.randomUUID();
        String uuidString = newUuid.toString();
        String encodedUserPassword = user.getPassword();
        Date expireDate = Timestamp.valueOf(LocalDateTime.now().plusSeconds(lifetime));
        PasswordSecurityToken securityToken = new PasswordSecurityToken(uuidString, expireDate, user, encodedUserPassword);
        securityToken = passwordSecurityTokenRepository.save(securityToken);
        return securityToken;
    }

}
