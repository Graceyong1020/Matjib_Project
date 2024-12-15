package org.grace.matjibbacked.security.filter;

import com.google.gson.Gson;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.dto.MemberDTO;
import org.grace.matjibbacked.util.JWTUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;


@Log4j2
public class JWTCheckFilter extends OncePerRequestFilter {
    // JWT 토큰이 유효한지 확인하는 필터


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        log.info("check uri--------------" + path);

        //api/member/ 이하 경로는 필터를 타지 않도록 설정
        if (path.startsWith("/api/member/")) {
            return true;
        }
        // image 조회 경로 체크 -> 필터 타지 않도록 설정
        if (path.startsWith("/api/products/view/")) {
            return true;
        }

        if(path.startsWith("/api/todo/")){
            log.info("-------------------------shouldNotFilter api");
            return true;
        }


        return false; // false == check, true == not check
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        log.info("--------------------");

        log.info("--------------------");

        log.info("--------------------");

        //헤더에서 토큰 추출
        String authHeaderStr = request.getHeader("Authorization");
        try {
            //Bearer -7글자 (JWT 문자열) 제외한 나머지를 추출
            String accessToken = authHeaderStr.substring(7);
            Map<String, Object> claims = JWTUtil.validateToken(accessToken); //토큰 유효성 검사

            log.info(claims);

            //destination
            // filterChain.doFilter(request, response); //이 필터에 대한 등록은 CustomSecurityConfig에서 함
            //security context holder에 claims를 저장

            // 성공하면 사용자에 대한 정보를 가져와서  MemberDTO 객체를 생성
            String email = (String) claims.get("email");
            String pw = (String) claims.get("pw");
            String nickname = (String) claims.get("nickname");
            Boolean social = (Boolean) claims.get("social");
            List<String> roleNames = (List<String>) claims.get("roleNames");


            if (roleNames != null) {
                roleNames.stream()
                        .forEach(role -> {
                            // Process each role
                        });
            } else {
                log.error("roleNames is null");
                // Handle the case where roleNames is null, e.g., send an error response
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token: roleNames is null");
                return;
            }

            MemberDTO memberDTO = new MemberDTO(email, pw, nickname, social.booleanValue(), roleNames);

            log.info("-----------------------------------");
            log.info(memberDTO);
            log.info(memberDTO.getAuthorities());

            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(memberDTO, pw, memberDTO.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            filterChain.doFilter(request, response);

        }catch(Exception e){

            log.error("JWT Check Error..............");
            log.error(e.getMessage());

            Gson gson = new Gson();
            String msg = gson.toJson(Map.of("error", "ERROR_ACCESS_TOKEN"));

            response.setContentType("application/json");
            PrintWriter printWriter = response.getWriter();
            printWriter.println(msg);
            printWriter.close();

        }
    }


}
