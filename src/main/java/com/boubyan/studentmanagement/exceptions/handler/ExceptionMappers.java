package com.boubyan.studentmanagement.exceptions.handler;

import com.boubyan.studentmanagement.exceptions.customexceptions.BadRequestException;
import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;
import com.boubyan.studentmanagement.exceptions.customexceptions.SystemException;
import com.boubyan.studentmanagement.exceptions.models.ErrorResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ExceptionMappers {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(HttpServletRequest req, MethodArgumentNotValidException ex) {
        var error = new ErrorResponse(HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
        for (ObjectError item : ex.getAllErrors()) {
            error.addValidationError(item.getCode(), item.getDefaultMessage());
        }
        return new ResponseEntity<>(error, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(HttpServletRequest req, BusinessException ex) {
        var error = new ErrorResponse(ex.getStatus().value(), ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(HttpServletRequest req, RuntimeException ex) {
        var error = new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage());
        ex.printStackTrace();
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<ErrorResponse> handleSystemException(HttpServletRequest req, SystemException ex) {
        var error = new ErrorResponse(ex.getStatus().value(), ex.getMessage());
        return new ResponseEntity<>(error, ex.getStatus());
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(HttpServletRequest req, AccessDeniedException ex) {
        var error = new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(HttpServletRequest req, BadRequestException ex) {
        var error = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> handleNullPointerException(HttpServletRequest req, NullPointerException ex) {
        ex.printStackTrace();
        ErrorResponse error = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.fillInStackTrace().getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}