package org.grace.matjibbacked.config;

import lombok.extern.log4j.Log4j2;
import org.grace.matjibbacked.controller.formatter.LocalDateFormatter;
import org.grace.matjibbacked.dto.TodoDTO;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Map;

@Configuration
@Log4j2
public class CustomServletConfig implements WebMvcConfigurer { // WebMvcConfigurer 인터페이스를 구현하여 addFormatters 메서드를 오버라이딩
    //날짜형식 처리
    @Override
    public void addFormatters(FormatterRegistry registry) {

        log.info("addFormatters..........");
        registry.addFormatter(new LocalDateFormatter());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .maxAge(500)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD");
    }


}
