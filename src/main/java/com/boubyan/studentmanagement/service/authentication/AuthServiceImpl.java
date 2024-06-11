package com.boubyan.studentmanagement.service.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;
import com.boubyan.studentmanagement.mapper.JWTMapper;
import com.boubyan.studentmanagement.model.Credentials;
import com.boubyan.studentmanagement.model.LoginResponse;
import com.boubyan.studentmanagement.repository.UserRepository;
import com.boubyan.studentmanagement.security.jwt.JwtValidation;
import com.boubyan.studentmanagement.security.service.AuthenticationService;
import com.boubyan.studentmanagement.utils.JWTUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component("AuthenticationServiceImpl")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;

    private final AuthenticationService authenticationService;

    private final UserRepository studentRepository;
    private final JwtValidation jwtValidation;
    private final JWTUtils jwtUtils;


    @Override
    public LoginResponse login(Credentials credentials) {
        log.debug("login started...");
        var user = studentRepository.findUserByUsername(credentials.getUsername())
                .orElse(null);
        if (user != null) {
            var authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword()));
            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            var jwt = authenticationService.generateJwtToken(authentication, user);
            var jwtRefreshToken = authenticationService.generateRefreshToken(user);
            LoginResponse loginResponse = JWTMapper.mapLoginResponse(jwt, jwtRefreshToken);
            log.debug("login end successfully.");
            return loginResponse;
        }
        log.debug("user not found");
        throw new BusinessException("User Login failed!", HttpStatus.UNAUTHORIZED);
    }

    @Override
    public LoginResponse generateAccessTokenFromRefreshToken(String refreshToken) {
        log.debug("generate access token from refresh token Token...");
        if (jwtValidation.validateJwtToken(refreshToken)) {

            String username = jwtUtils.getUsernameFromJwtToken(refreshToken);
            var user = studentRepository.findUserByUsername(username)
                    .orElse(null);
            if (user != null) {
                var jwt = authenticationService.generateJwtTokenFromRefresh(user, jwtUtils.getExpirationDateFromJwtToken(refreshToken));
                LoginResponse loginResponse = JWTMapper.mapLoginResponse(jwt, refreshToken);
                log.debug("token generated successfully.");
                return loginResponse;
            }
        }
        log.debug("invalid refresh token.");
        throw new BadRequestException("invalid refresh token");
    }


}
