package com.explosion204.wclookup.security;

import com.explosion204.wclookup.model.repository.UserRepository;
import com.explosion204.wclookup.service.util.TokenUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static com.explosion204.wclookup.security.ApplicationAuthority.ADMIN;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String AUTH_ENDPOINTS = "/api/auth/**";
    private static final String USERS_ENDPOINTS = "/api/users/**";

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    public SecurityConfig(TokenUtil tokenUtil, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(AUTH_ENDPOINTS)
                .antMatchers("/h2-console/**"); // TODO: 10/12/2021
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(new JwtFilter(authenticationManager()), BasicAuthenticationFilter.class)
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                    .antMatchers(USERS_ENDPOINTS).hasAuthority(ADMIN.getAuthority());
    }

    @Override
    protected AuthenticationManager authenticationManager() {
        return new JwtAuthenticationManager(tokenUtil, userRepository);
    }
}
