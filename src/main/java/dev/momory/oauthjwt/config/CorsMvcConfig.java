package dev.momory.oauthjwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * CORS 설정을 위한 Spring WebMvcConfigurer 구현 클래스
 */

@Configuration
public class CorsMvcConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry corsRegistry) {

        // CORS 설정을 추가하는 메서드
        corsRegistry.addMapping("/**") // 모든 경로에 대해 CORS를 허용함
                .exposedHeaders("Set-Cookie") // 클라이언트가 응답 헤더의 'Set-Cookie' 를 접근할 수 있게 허용함
                .allowedOrigins("http://localhost:5173"); // 설정한 도메인 에서의 요청을 허용함
    }
}
