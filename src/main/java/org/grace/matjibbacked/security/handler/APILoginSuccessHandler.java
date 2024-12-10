package org.grace.matjibbacked.security.handler;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.dto.MemberDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Log4j2
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        log.info("login success");
        log.info(authentication);
        log.info("login success");

        MemberDTO memberDTO = (MemberDTO) authentication.getPrincipal();

       Map<String, Object> claims = memberDTO.getClaims();

       claims.put("accessToken", "");
       claims.put("refreshToken", "");

        Gson gson = new Gson();

        String jsonStr = gson.toJson(claims);

        response.setContentType("application/json; charset=utf-8"); // json 형태로 반환

        PrintWriter printWriter = response.getWriter(); // response 객체를 이용해 클라이언트에게 응답을 보낼 수 있는 PrintWriter 객체를 얻어옴
        printWriter.println(jsonStr);
        printWriter.close();


    }
}
