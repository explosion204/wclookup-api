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

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();

        userDto.id = user.getId();
        userDto.googleId = user.getGoogleId();
        userDto.nickname = user.getNickname();
        userDto.refreshToken = user.getRefreshToken();
        userDto.refreshTokenExpiration = user.getRefreshTokenExpiration();
        userDto.isAdmin = user.isAdmin();

        return userDto;
    }
}
