package com.boubyan.studentmanagement.mapper;

import com.boubyan.studentmanagement.model.LoginResponse;

public class JWTMapper {

    private JWTMapper() {

    }

    /**
     * map the login response from the given jwt accessToken and the jwt refreshToken.
     *
     * @param accessToken  access token to be mapped
     * @param refreshToken refresh token to be mapped
     * @return LoginResponse
     */
    public static LoginResponse mapLoginResponse(String accessToken, String refreshToken) {
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setRefreshToken(refreshToken);
        loginResponse.setTokenType("Bearer");
        return loginResponse;
    }
}
