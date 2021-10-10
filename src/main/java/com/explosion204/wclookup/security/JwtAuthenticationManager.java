package com.explosion204.wclookup.security;

import com.explosion204.wclookup.model.entity.User;
import com.explosion204.wclookup.model.repository.UserRepository;
import com.explosion204.wclookup.service.UserService;
import com.explosion204.wclookup.service.util.TokenUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

import static com.explosion204.wclookup.security.ApplicationAuthority.ADMIN;
import static com.explosion204.wclookup.security.ApplicationAuthority.USER;

@Component
public class JwtAuthenticationManager implements AuthenticationManager {
    private static final String USER_ID_CLAIM = "user_id";

    private final TokenUtil tokenUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationManager(TokenUtil tokenUtil, UserRepository userRepository) {
        this.tokenUtil = tokenUtil;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthentication jwtAuthentication = (JwtAuthentication) authentication;
        String accessToken = jwtAuthentication.getAccessToken();

        Map<String, Object> claims = tokenUtil.parseToken(accessToken);

        if (claims.containsKey(USER_ID_CLAIM)) {
            long userId = ((Double) claims.get(USER_ID_CLAIM)).longValue();
            User user = userRepository.getById(userId);
            jwtAuthentication.setAuthorities(List.of(user.isAdmin() ? ADMIN : USER));
            jwtAuthentication.setUser(user);
            jwtAuthentication.setAuthenticated(true);
        }

        return jwtAuthentication;
    }
}
