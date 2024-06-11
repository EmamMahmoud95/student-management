package com.boubyan.studentmanagement.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.boubyan.studentmanagement.entity.User;
import com.boubyan.studentmanagement.mapper.StudentMapper;
import com.boubyan.studentmanagement.payload.response.UserInformation;
import com.boubyan.studentmanagement.utils.JWTUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationService {

    private final StudentMapper userMapper;

    private final JWTUtils jwtUtils;
    @Value("${boubyan.app.jwtExpirationMs}")
    private Integer jwtExpirationMs;
    @Value("${boubyan.app.jwtRefreshTokenExpirationMs}")
    private Integer jwtRefreshTokenExpirationMs;
    @Value("${boubyan.app.issuer.name:boubyan.com}")
    private String issuer;
    private static final String USER_INFORMATION = "user-information";

    public String generateJwtToken(Authentication authentication, User user) {
        UserInformation userInformation = userMapper.mapUserInformationJWT(user);
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject((userDetails.getUsername()))
                .claim(USER_INFORMATION, userInformation)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.RS512, jwtUtils.getPrivateKey())
                .compact();
    }

    public String generateJwtTokenFromRefresh(User user, Date expirationDateFromJwtToken) {
        UserInformation userInformation = userMapper.mapUserInformationJWT(user);
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject((user.getUsername()))
                .claim(USER_INFORMATION, userInformation)
                .setIssuedAt(new Date())
                .setExpiration(minDate(expirationDateFromJwtToken, new Date((new Date()).getTime() + jwtExpirationMs)))
                .signWith(SignatureAlgorithm.RS256, jwtUtils.getPrivateKey())
                .compact();
    }

    public static Date minDate(Date date1, Date date2) {
        return date1.before(date2) ? date1 : date2;

    }

    public String generateRefreshToken(User user) {
        UserInformation userInformation = userMapper.mapUserInformationJWT(user);
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject((user.getUsername()))
                .claim(USER_INFORMATION, userInformation)
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtRefreshTokenExpirationMs))
                .signWith(SignatureAlgorithm.RS256, jwtUtils.getPrivateKey())
                .compact();
    }

}
