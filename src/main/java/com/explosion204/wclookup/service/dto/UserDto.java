package com.explosion204.wclookup.service.dto;

import com.explosion204.wclookup.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class UserDto extends IdentifiableDto {
    private String googleId;
    private String nickname;
    private String refreshToken;
    private LocalDateTime refreshTokenExpiration;
    private boolean isAdmin;

    public User toUser() {
        User user = new User();

        user.setId(getId());
        user.setGoogleId(googleId);
        user.setNickname(nickname);
        user.setRefreshToken(refreshToken);
        user.setRefreshTokenExpiration(refreshTokenExpiration);
        user.setAdmin(isAdmin);

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setGoogleId(user.getGoogleId());
        userDto.setNickname(user.getNickname());
        userDto.setRefreshToken(user.getRefreshToken());
        userDto.setRefreshTokenExpiration(user.getRefreshTokenExpiration());
        userDto.setAdmin(user.isAdmin());

        return userDto;
    }
}
