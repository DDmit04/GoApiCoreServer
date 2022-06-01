package com.goapi.goapi.config;

import com.goapi.goapi.security.JwtUtils;
import com.goapi.goapi.security.auth.JwtAuthProvider;
import com.goapi.goapi.security.filter.JwtAuthFilter;
import com.goapi.goapi.security.filter.UsernameEmailAuthFilter;
import com.goapi.goapi.security.handler.AuthSuccessfulHandler;
import com.goapi.goapi.security.handler.LogOutSuccessHandler;
import com.goapi.goapi.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;

/**
 * @author Daniil Dmitrochenkov
 **/
@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService userService;
    private final JwtUtils jwtTokenUtil;

    private final Environment env;
    private final PasswordEncoder passwordEncoder;

    private final AuthSuccessfulHandler authSuccessfulHandler;
    private final LogOutSuccessHandler logOutHandler;

    private final JwtAuthProvider jwtAuthProvider;

    @Value("${my.url.login}")
    private String loginUrl;

    @Value("${my.url.logout}")
    private String logoutUrl;

    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public FilterRegistrationBean<UsernamePasswordAuthenticationFilter> usernameEmailAuthenticationFilter() throws Exception {
        FilterRegistrationBean<UsernamePasswordAuthenticationFilter> registrationBean = new FilterRegistrationBean<>();
        UsernamePasswordAuthenticationFilter filter = new UsernameEmailAuthFilter(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(authSuccessfulHandler);
        filter.setFilterProcessesUrl(loginUrl);
        registrationBean.setFilter(filter);
        return registrationBean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String authCookieName = env.getProperty("jwt.token.name.accessCookieName");
        JwtAuthFilter jwtFilter = new JwtAuthFilter(authenticationManagerBean(), jwtTokenUtil, env);


        http.cors().and().csrf().disable();

        http.sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and();

        http.logout()
            .logoutUrl(logoutUrl)
            .addLogoutHandler(logOutHandler)
            .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK))
            .deleteCookies(authCookieName, "JSESSIONID")
            .invalidateHttpSession(true)
            .permitAll()
            .and()
            .addFilterBefore(
                jwtFilter,
                LogoutFilter.class
            )
            .authorizeRequests()
            .antMatchers(loginUrl).permitAll()
            .antMatchers("/db/**").permitAll()
            .antMatchers("/api/**").permitAll()
            .antMatchers("/jwt/**").permitAll()
            .antMatchers("/user/**").authenticated()
            .and()
            .addFilterBefore(
                jwtFilter,
                UsernamePasswordAuthenticationFilter.class
            )
            .authorizeRequests()
            .anyRequest().authenticated();


    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
        auth.authenticationProvider(jwtAuthProvider);
    }
}
