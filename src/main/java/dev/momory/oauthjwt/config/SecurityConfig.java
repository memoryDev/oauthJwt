package dev.momory.oauthjwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{

        // HttpSecurity 사용하여 보안 설정을 정의
        http
                // CSRF 보호 기능을 비활성화합니다(JWT 사용시 보통 비활성화함)
                .csrf(csrf -> csrf.disable())
                // 폼 로그인 방식을 비활성화함(OAuth2 로그인 할것이기떄문에)
                .formLogin(auth -> auth.disable())
                // Http Basic 인증 방식을 비활성화함(JWT 사용으로인해)
                .httpBasic(auth -> auth.disable())
                // OAuth2 로그인을 사용하도록 설정
                .oauth2Login(Customizer.withDefaults())
                .authorizeHttpRequests((auth) -> {
                    // "/" 경로에 대한 접근을 모든 사용자에게 허용
                    auth.requestMatchers("/").permitAll()
                            // 그외의 모든 요청은 인증된 사용자만 접근가능
                            .anyRequest().authenticated();
                })
                .sessionManagement(session ->
                        // 세션 관리를 STATELESS로 설정함(JWT 사용시 서버에 세션을 저장하지 않음)
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //설정된 보안 필터 체인 반환
        return http.build();
    }
}
