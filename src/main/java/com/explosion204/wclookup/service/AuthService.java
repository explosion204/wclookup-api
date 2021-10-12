package com.explosion204.wclookup.service;

import com.explosion204.wclookup.model.entity.User;
import com.explosion204.wclookup.model.repository.UserRepository;
import com.explosion204.wclookup.service.dto.AuthDto;
import com.explosion204.wclookup.service.util.TokenUtil;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;

@Service
public class AuthService {
    private static final String USER_ID_CLAIM = "user_id";

    @Value("${google.oauth.client_id}")
    private String clientId;

    @Value("${refresh.validity_time}")
    private int refreshValidityTime;

    private final UserRepository userRepository;
    private final TokenUtil tokenUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, TokenUtil tokenUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenUtil = tokenUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public AuthDto authenticate(String googleAccessToken) {
        GoogleIdToken googleIdToken = parseGoogleAccessToken(googleAccessToken)
            .orElseThrow(() -> new BadCredentialsException(StringUtils.EMPTY));
        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        String googleId = payload.getSubject();
        User user = userRepository.findByGoogleId(googleId)
                .orElseGet(() -> createUser(googleId));

        return buildAuthDto(user);
    }

    public AuthDto refresh(String refreshToken) {
        User user = userRepository.findByRefreshToken(passwordEncoder.encode(refreshToken))
                .orElseThrow(() -> new CredentialsExpiredException(StringUtils.EMPTY));

        return buildAuthDto(user);
    }

    private AuthDto buildAuthDto(User user) {
        String accessToken = tokenUtil.generateJwt(
                Map.of(USER_ID_CLAIM, user.getId())
        );

        String refreshToken = tokenUtil.generateRefreshToken();
        LocalDateTime expirationTime = LocalDateTime.now(UTC).plus(refreshValidityTime, ChronoUnit.DAYS);
        user.setRefreshToken(passwordEncoder.encode(refreshToken));
        user.setRefreshTokenExpiration(expirationTime);
        userRepository.save(user);

        AuthDto authDto = new AuthDto();
        authDto.setAccessToken(accessToken);
        authDto.setRefreshToken(refreshToken);

        return authDto;
    }

    @SneakyThrows
    private Optional<GoogleIdToken> parseGoogleAccessToken(String accessToken) {
        HttpTransport httpTransport = new NetHttpTransport();
        JsonFactory jsonFactory = new GsonFactory();
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(httpTransport, jsonFactory)
                .build();

        GoogleIdToken idToken = verifier.verify(accessToken);
        return Optional.ofNullable(idToken);
    }

    private User createUser(String googleId) {
        User newUser = new User();

        newUser.setGoogleId(googleId);
        newUser.setNickname(StringUtils.EMPTY);
        newUser.setAdmin(false);

        return userRepository.save(newUser);
    }
}
