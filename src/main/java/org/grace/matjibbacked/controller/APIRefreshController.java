package org.grace.matjibbacked.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.util.CustomJWTException;
import org.grace.matjibbacked.util.JWTUtil;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Log4j2
public class APIRefreshController {
    // token 갱신하고 싶을때

    @RequestMapping("/api/member/refresh")
    public Map<String, Object> refresh(
            @RequestHeader("Authorization") String authHeader,
            String refreshToken) { // authHeader: accessToken

        if (refreshToken == null) {
            throw new CustomJWTException("NULL_REFRESH");
        }
        // accessToken이 없거나 길이가 7보다 작다면
        if (authHeader == null || authHeader.length() < 7) {
            throw new CustomJWTException("INVALID_STRING");
        }
        String accessToken = authHeader.substring(7);

        //Access 토큰이 만료되지 않았다면
        if (checkExpiredToken(accessToken) == false) {
            return Map.of("accessToken", accessToken, "refreshToken", refreshToken);
        }// 기존 토큰 가지고 나가

        //Refresh토큰 검증
        Map<String, Object> claims = JWTUtil.validateToken(refreshToken);

        log.info("refresh ... claims: " + claims);

        String newAccessToken = JWTUtil.generateToken(claims, 10);

        String newRefreshToken = checkTime((Integer) claims.get("exp")) == true ? JWTUtil.generateToken(claims, 60 * 24) : refreshToken;

        return Map.of("accessToken", newAccessToken, "refreshToken", newRefreshToken);

    }

    // 시간이 1시간 미만으로 남았다면
    private boolean checkTime(Integer exp) {
        //JWT exp를 날짜로
        java.util.Date expDate = new java.util.Date((long) exp * (1000));
        //현재 시간 차이 계산 - milliseconds
        long gap = expDate.getTime() - System.currentTimeMillis();
        // 분단위 계산
        long leftMin = gap / (1000 * 60);
        // 60분 이하라면
        return leftMin < 60;
    }

    // 토큰이 만료되었는지 확인
    private boolean checkExpiredToken(String token) {

        try {
            JWTUtil.validateToken(token);
        } catch (CustomJWTException ex) {
            if (ex.getMessage().equals("Expired")) {
                return true;
            }
        }
        return false;
    }
}

