package com.boubyan.studentmanagement.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;
import com.boubyan.studentmanagement.model.Credentials;
import com.boubyan.studentmanagement.model.LoginResponse;
import com.boubyan.studentmanagement.payload.response.JwtResponse;
import com.boubyan.studentmanagement.repository.UserRepository;
import com.boubyan.studentmanagement.security.jwt.JwtValidation;
import com.boubyan.studentmanagement.security.service.AuthenticationService;
import com.boubyan.studentmanagement.service.authentication.AuthServiceImpl;
import com.boubyan.studentmanagement.utils.JWTUtils;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthServiceImpl
            authService;

    @Mock
    private UserRepository studentRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private JwtValidation jwtValidation;

    @Mock
    private JWTUtils jwtUtils;

    @Test
    public void testLogin() {
        Credentials credentials = new Credentials("test@example.com", "password123");
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        when(studentRepository.findUserByUsername(any())).thenReturn(Optional.ofNullable(user));
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        String jwtToken = "token";
        when(authenticationService.generateJwtToken(authentication, user)).thenReturn(jwtToken);
        when(authenticationService.generateRefreshToken(user)).thenReturn(jwtToken);
        JwtResponse jwtResponse = new JwtResponse();
        LoginResponse result = authService.login(credentials);
        Assertions.assertEquals("token", result.getAccessToken());
        verify(authenticationManager, times(1)).authenticate(any());
        verify(authenticationService, times(1)).generateJwtToken(any(), any());
        verify(authenticationService, times(1)).generateRefreshToken(any());
    }

    @Test
    public void testLoginUserNotFound() {
        Credentials credentials = new Credentials("test@example.com", "password123");
        when(studentRepository.findUserByUsername(any())).thenReturn(Optional.empty());
        assertThrows(BusinessException.class, () -> authService.login(credentials));
        verify(authenticationManager, times(0)).authenticate(any());
        verify(authenticationService, times(0)).generateJwtToken(any(), any());
    }

    @Test
    public void refreshToken() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");
        when(jwtValidation.validateJwtToken(any())).thenReturn(true);
        when(jwtUtils.getUsernameFromJwtToken(any())).thenReturn("test");
        when(jwtUtils.getExpirationDateFromJwtToken(any())).thenReturn(new Date());
        when(studentRepository.findUserByUsername(any())).thenReturn(Optional.ofNullable(user));
        String jwtToken = "token";
        String jwtRefreshToken = "jwtRefreshToken";
        when(authenticationService.generateJwtTokenFromRefresh(any(), any())).thenReturn(jwtToken);
        LoginResponse result = authService.generateAccessTokenFromRefreshToken(jwtRefreshToken);
        Assertions.assertEquals("token", result.getAccessToken());
        Assertions.assertEquals("jwtRefreshToken", result.getRefreshToken());
        Assertions.assertEquals("Bearer", result.getTokenType());
        verify(authenticationService, times(1)).generateJwtTokenFromRefresh(any(), any());
    }

    @Test
    public void refreshTokenNotVaild() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test");

        String jwtRefreshToken = "jwtRefreshToken";
        Assertions.assertThrows(BadRequestException.class, () -> authService.generateAccessTokenFromRefreshToken(jwtRefreshToken));

    }

}
