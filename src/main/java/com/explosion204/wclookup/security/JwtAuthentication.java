package com.explosion204.wclookup.security;

import com.explosion204.wclookup.model.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.List;

import static com.explosion204.wclookup.security.ApplicationAuthority.ADMIN;
import static com.explosion204.wclookup.security.ApplicationAuthority.USER;

public class JwtAuthentication implements Authentication {
    private Collection<? extends GrantedAuthority> authorities;
    private User user;
    private String accessToken;
    private boolean isAuthenticated;

    public JwtAuthentication(String accessToken) {
        //
        authorities = List.of();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return accessToken;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    @Override
    public Object getPrincipal() {
        return user.getId();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) {
        this.isAuthenticated = isAuthenticated;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
