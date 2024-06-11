package com.boubyan.studentmanagement.utils;

import com.boubyan.studentmanagement.exceptions.customexceptions.BusinessException;

import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JWTUtils {
    private static final String ppk = "/keys/private.ppk";
    private static final String pub = "/keys/public.pub";

    private final ResourceLoader resourceLoader;

    @Value("${key.privatekey}")
    private Resource privateKey;

    @Value("${key.publickey}")
    private Resource  publicKey;


    public String getUsernameFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            log.error("Cannot set user authentication: ", e);
        }
        return null;
    }
    public Date getExpirationDateFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(token).getBody().getExpiration();
        } catch (Exception e) {
            log.error("Cannot set user authentication: ", e);
        }
        return null;
    }

    public String getJsonWebToken(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.replace("Bearer ", "");
        }
        return null;
    }

    public PrivateKey getPrivateKey() {
        try {
            var ppkString = readFileContent(privateKey.getFile());
            var privateKeyString = ppkString.replace("\\s+", "");
            var keyFactory = KeyFactory.getInstance("RSA");
            var privateKeyBytes = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString));

            return keyFactory.generatePrivate(privateKeyBytes);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw keyGenerationError();
        }
    }


    public PublicKey getPublicKey() {
        try {

            var pubString = readFileContent(publicKey.getFile());
            var publicKeyString = pubString.replace("\\s+", "");
            var keyFactory = KeyFactory.getInstance("RSA");
            var publicKeyBytes = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString));

            return keyFactory.generatePublic(publicKeyBytes);
        } catch (IOException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            e.printStackTrace();
            throw keyGenerationError();
        }
    }


    private String readFileContent(File file) throws IOException {
        var stringBuilder = new StringBuilder();
        var reader = new BufferedReader(new FileReader(file));
        var line = "";
        while ((line = reader.readLine()) != null) {
            stringBuilder.append(line);
        }
        reader.close();
        return stringBuilder.toString();
    }


    private BusinessException keyGenerationError() {
        return new BusinessException("Sorry!! We faced an issue during Key generation", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
