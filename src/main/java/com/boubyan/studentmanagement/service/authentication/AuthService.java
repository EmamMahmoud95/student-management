package com.boubyan.studentmanagement.service.authentication;

import org.springframework.stereotype.Service;

import com.boubyan.studentmanagement.model.Credentials;
import com.boubyan.studentmanagement.model.LoginResponse;

@Service
public interface AuthService {

    /**
     * this method allow the user to login using his Credentials.
     *
     * @param credentials user Credentials that contains the email and password that is needed for login
     * @return LoginResponse
     */
    LoginResponse login(Credentials credentials);

    /**
     * this method allow the user to refresh the jwt token from the refresh token.
     *
     * @param refreshToken refresh token to authenticate the user
     * @return LoginResponse
     */
    LoginResponse generateAccessTokenFromRefreshToken(String refreshToken);
}
