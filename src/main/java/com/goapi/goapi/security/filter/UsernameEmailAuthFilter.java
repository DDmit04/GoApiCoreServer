package com.goapi.goapi.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goapi.goapi.controller.form.user.UserAuthForm;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author Daniil Dmitrochenkov
 **/
public class UsernameEmailAuthFilter extends UsernamePasswordAuthenticationFilter {

    public UsernameEmailAuthFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        ObjectMapper mapper = new ObjectMapper();
        UserAuthForm userAuthForm;
        try {
            userAuthForm = mapper.readValue(request.getInputStream(), UserAuthForm.class);
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            Validator validator = factory.getValidator();
            Set<ConstraintViolation<UserAuthForm>> violations = validator.validate(userAuthForm);
            if (!violations.isEmpty()) {
                throw new BadCredentialsException("Invalid login request params!");
            }
        } catch (IOException e) {
            throw new BadCredentialsException("Invalid login request params!");
        }

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
            userAuthForm.getUsernameOrEmail(),
            userAuthForm.getPassword(),
            new ArrayList<>()
        );
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
