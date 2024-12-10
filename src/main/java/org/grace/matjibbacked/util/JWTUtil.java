package org.grace.matjibbacked.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.io.UnsupportedEncodingException;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;

public class JWTUtil {
    // JWT 토큰 생성 및 검증을 위한 클래스
    private static String key = "12346789123456789123456789123456789";

    public static String generateToken(Map<String, Object> valueMap, int min) {
        //JWT 문자열 만드는 기능. min은 토큰 유효시간
        SecretKey key = null; // 비밀키 생성

        try {
            key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        String jwtStr = Jwts.builder()
                .setHeader(Map.of("typ", "JWT"))
                .setClaims(valueMap)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(min).toInstant()))
                .signWith(key)
                .compact();
        return jwtStr;

    }

    public static Map<String, Object> validateToken(String token) {
        Map<String, Object> claim = null;

        try {

            SecretKey key = Keys.hmacShaKeyFor(JWTUtil.key.getBytes("UTF-8"));

            claim = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token) // parsing or verifying. if token is invalid, throw exception
                    .getBody();
        } catch (MalformedJwtException malformedJwtException) {
            throw new CustomJWTException(("MalFormed"));
        } catch (ExpiredJwtException expiredJwtException) {
            throw new CustomJWTException(("Expired"));
        } catch (InvalidClaimException invalidClaimException) {
            throw new CustomJWTException("Invalid");
        } catch (JwtException jwtException) {
            throw new CustomJWTException("JWTError");
        } catch (Exception e) {
            throw new CustomJWTException("Error");
        }
        return claim;

    }
}

