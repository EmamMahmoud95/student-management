package com.boubyan.studentmanagement.security.jwt;

import org.springframework.stereotype.Component;

import com.boubyan.studentmanagement.repository.UserRepository;
import com.boubyan.studentmanagement.utils.JWTUtils;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtValidation {

    private final UserRepository studentRepository;

    private final JWTUtils jwtUtils;

    public boolean validateJwtToken(String authToken) {
        try {
            var claims = Jwts.parser()
                    .setSigningKey(jwtUtils.getPublicKey())
                    .parseClaimsJws(authToken);
            var username = jwtUtils.getUsernameFromJwtToken(authToken);

            var exp = claims.getBody()
                    .getExpiration();
            var user = studentRepository.findUserByUsername(username)
                    .orElseThrow(this::getSignatureException);
            var issuedAt = claims.getBody()
                    .getIssuedAt();
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    private SignatureException getSignatureException() {
        return new SignatureException("invalid token");
    }
}
