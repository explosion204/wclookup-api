package com.explosion204.wclookup.service.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthDto {
    private String accessToken;
    private String refreshToken;
}
