package dev.momory.oauthjwt.config;

import dev.momory.oauthjwt.jwt.JWTFilter;
import dev.momory.oauthjwt.jwt.JWTUtil;
import dev.momory.oauthjwt.oauth2.CustomSuccessHandler;
import dev.momory.oauthjwt.service.CustomOAuth2UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

/**
 * Spring Security 설정 클래스
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService; //OAuth2 사용자 서비스
    private final CustomSuccessHandler customSuccessHandler; // 인증 성공 시 동작을 정의하는 핸들러
    private final JWTUtil jwtUtil; // JWT 유틸리티

    /**
     * Spring Security 필터 체인을 설정하는 메서드
     * @param http HTTP 보안 설정을 위한 매개변수
     * @return SecurityFilterChain 보안 필터 체인
     * @throws Exception 설정 중 발생할 수 있는 예외
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 기능 비활성화(API통신시 기본적으로 비활성화하여 사용)
                .formLogin(auth -> auth.disable()) // 기본 폼 로그인 기능 비활성화
                .httpBasic(auth -> auth.disable()) // HTTP Basic 인증 비활성화
                // JWT 필터를 OAuth2 로그인 필터 이후에 추가
                .addFilterAfter(new JWTFilter(jwtUtil), OAuth2LoginAuthenticationFilter.class)
                .oauth2Login((oauth2) -> oauth2
                        .userInfoEndpoint((userInfoEndpointConfig -> userInfoEndpointConfig
                                .userService(customOAuth2UserService))) // 사용자 정보 서비서 슬정
                        .successHandler(customSuccessHandler)) // 인증 성공시 핸들러 설정
                .authorizeHttpRequests((auth) -> {
                    auth.requestMatchers("/").permitAll() // 루트("/") 경로에 대한 접근을 모두 허용
                            .anyRequest().authenticated(); // 그 외의 요청은 인증 필요
                })
                .sessionManagement(session ->
                        // 세션을 사용하지 않음(JWT 토큰 사용)
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // CORS 설정
        http
                .cors(corsCustomizer -> corsCustomizer.configurationSource(new CorsConfigurationSource() {

                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                        CorsConfiguration configuration = new CorsConfiguration();

                        // CORS 설정

                        // 허용할 출처
                        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:5173"));
                        // 모든 HTTP 메서드 허용(GET, POST ...)
                        configuration.setAllowedMethods(Collections.singletonList("*"));
                        // 자격 증명 허용
                        configuration.setAllowCredentials(true);
                        // 모든 헤더 허용
                        configuration.setAllowedHeaders(Collections.singletonList("*"));
                        // 설정의 캐싱 시간(초)
                        configuration.setMaxAge(3600L);

                        // 클라이언트가 접근 가능한 헤더 지정
                        configuration.setExposedHeaders(Collections.singletonList("Set-Cookie"));
                        // 클라이언트가 접근 가능한 헤더 지정
                        configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                        return configuration;
                    }
                }));

        //설정된 보안 필터 체인 반환
        return http.build();
    }
}
