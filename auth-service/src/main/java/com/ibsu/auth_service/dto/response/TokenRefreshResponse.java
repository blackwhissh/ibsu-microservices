package com.ibsu.auth_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class TokenRefreshResponse {
    private String accessToken;
    private String refreshToken;

    public String getTokenType() {
        return "Bearer";
    }
}
