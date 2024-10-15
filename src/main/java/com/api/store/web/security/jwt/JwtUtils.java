package com.api.store.web.security.jwt;

import com.api.store.web.security.EncryptorDecoder;
import com.api.store.web.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${store.app.jwtSecret}")
    private String jwtSecret;

    @Value("${store.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Autowired
    EncryptorDecoder encryptorDecoder;

    public String generateJwtTokenMasked(Authentication authentication) {

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwt = Jwts.builder()
                .setSubject((encryptorDecoder.decryptUsername(userDetails.getUsername())))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .setId(userDetails.getXlmAddress())
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        String jwtBase64 = Base64.getEncoder().withoutPadding().encodeToString((jwt).getBytes());
        String mask = RandomStringUtils.randomAlphanumeric(21);
        String reversedString = reverseString(mask+jwtBase64);
        mask = RandomStringUtils.randomAlphanumeric(13);

        return mask+reversedString;
    }

    public String getJWTTokenUnmasked(String jwtTokenMasked){

        if (StringUtils.hasText(jwtTokenMasked)) {
            try{
                jwtTokenMasked = jwtTokenMasked.substring(13, jwtTokenMasked.length());
                String reversedString = reverseString(jwtTokenMasked);
                jwtTokenMasked = reversedString.substring(21, reversedString.length());


                String jwt = new String(Base64.getDecoder().decode(jwtTokenMasked));
                return jwt;
            }catch (Exception ex){
                System.out.println(ex);
            }
        }
        return "";
    }

    public Boolean hasValidHttpOnlyCookie(UserDetailsImpl userDetails, String httpOnlyCookieEncryptedValue){
        try{
            if(userDetails.getJwt().equals(encryptorDecoder.decryptCookie(httpOnlyCookieEncryptedValue, userDetails.getXlmAddress()))){
                return true;
            }else {
                return false;
            }
        }catch(Exception ex){
            return false;
        }
    }

    private String reverseString(String str){
        try{
            StringBuilder sb = new StringBuilder(str);
            sb.reverse();
            return sb.toString();
        }catch (Exception ex){
            System.err.println(ex);
        }
        return "";
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}