package com.boubyan.studentmanagement.security;

import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;
import com.boubyan.studentmanagement.security.service.UserDetailsImpl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityHelper")
public class SecurityHelper {

    public Long getUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(UserDetailsImpl.class::isInstance)
                .map(UserDetailsImpl.class::cast)
                .map(UserDetailsImpl::getId)
                .orElseThrow(this::authenticationException);
    }


    public BusinessException authenticationException() {
        return new BusinessException("Unauthorized", HttpStatus.UNAUTHORIZED);
    }


}
