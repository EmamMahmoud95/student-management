package com.boubyan.studentmanagement.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.boubyan.studentmanagement.mapper.JWTMapper;
import com.boubyan.studentmanagement.model.LoginResponse;

public class JWTMapperTest {

    @Test
    public void testGenerator() {
        LoginResponse jwtResponse = JWTMapper.mapLoginResponse("jwt_access_token", "jwt_refresh");
        assertEquals("jwt_access_token", jwtResponse.getAccessToken());
        assertEquals("jwt_refresh", jwtResponse.getRefreshToken());
        assertEquals("Bearer", jwtResponse.getTokenType());
    }
}
