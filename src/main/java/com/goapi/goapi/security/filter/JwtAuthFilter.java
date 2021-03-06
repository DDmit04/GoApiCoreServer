package com.goapi.goapi.security.filter;

import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.security.auth.JwtProvedAuthToken;
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
        boolean tokenIsValid = jwtTokenUtil.validateToken(token);
        if (!tokenIsValid) {
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
        String loginUrl = env.getProperty("urls.login");
        String requestURI = request.getRequestURI();
        boolean isLoginUrl = requestURI.startsWith(loginUrl);
        return isLoginUrl || !StringUtils.hasText(token);
    }

    private String getAccessToken(HttpServletRequest request) {
        String authHeaderName = env.getProperty("authorization.header");
        String authHeaderPrefix = env.getProperty("authorization.prefix");
        String authHeader = request.getHeader(authHeaderName);
        if (authHeader == null) {
            return "";
        }
        String token = authHeader.replace(authHeaderPrefix, "");
        return token;
    }
}
