package com.goapi.goapi.security.filter;

import com.goapi.goapi.security.auth.JwtProvedAuthToken;
import com.goapi.goapi.security.JwtUtils;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * @author Daniil Dmitrochenkov
 **/
public class JwtAuthFilter extends BasicAuthenticationFilter {

    private final JwtUtils jwtTokenUtil;
    private final Environment env;

    public JwtAuthFilter(AuthenticationManager authenticationManager, JwtUtils jwtTokenUtil, Environment env) {
        super(authenticationManager);
        this.jwtTokenUtil = jwtTokenUtil;
        this.env = env;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = getAccessToken(request);
        String username = jwtTokenUtil.getTokenSubject(token);
        if (!jwtTokenUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        try {
            JwtProvedAuthToken authToken = new JwtProvedAuthToken(username);
            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            this.getAuthenticationManager().authenticate(authToken);
            chain.doFilter(request, response);
        } catch (UsernameNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        final String token = getAccessToken(request);
        return !StringUtils.hasText(token);
    }

    private String getAccessToken(HttpServletRequest request) {
        if(request.getCookies() != null) {
            Optional<String> refreshToken = Arrays.stream(request.getCookies())
                .filter(c -> c.getName().equals(env.getProperty("jwt.token.name.accessCookieName")))
                .map(c -> c.getValue())
                .findFirst();
            return refreshToken.orElse("");
        }
        return "";
    }
}
