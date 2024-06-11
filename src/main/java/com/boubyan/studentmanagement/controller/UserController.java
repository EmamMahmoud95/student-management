package com.boubyan.studentmanagement.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.boubyan.studentmanagement.api.StudentsApi;
import com.boubyan.studentmanagement.model.Credentials;
import com.boubyan.studentmanagement.model.LoginResponse;
import com.boubyan.studentmanagement.model.RefreshToken;
import com.boubyan.studentmanagement.model.Student;
import com.boubyan.studentmanagement.service.authentication.AuthService;
import com.boubyan.studentmanagement.service.user.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController implements StudentsApi {

    private final AuthService authService;

    private final UserService studentService;

    @Override
    public ResponseEntity<LoginResponse> login(Credentials credentials) {
        return ResponseEntity.ok(authService.login(credentials));
    }

    @Override
    public ResponseEntity<LoginResponse> authRefreshTokenPost(RefreshToken refreshToken) {
        return ResponseEntity.ok(authService.generateAccessTokenFromRefreshToken(refreshToken.getRefreshToken()));
    }

    @Override
    public ResponseEntity<Void> signup(Student student) {
        studentService.insertStudent(student);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}
