package dev.momory.oauthjwt.jwt;

import dev.momory.oauthjwt.dto.CustomOAuth2User;
import dev.momory.oauthjwt.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT를 사용하여 인증된 사용자를 필터링하는 Spring Security 필터
 * OncePerRequestFilter를 확장하여 매 요청마다 실행됩니다.
 */
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스(토큰 검증 및 정보 추출에 사용함)

    /**
     * 요청을 필터링하고 인증 정보를 설정하는 메서드
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     * @throws ServletException 서블릿 관련 예외
     * @throws IOException 입출력 관련 예외
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;
        Cookie[] cookies = request.getCookies();

        // 요청의 쿠키에서 "Authorization" 쿠키를 찾음
        for (Cookie cookie : cookies) {
            System.out.println("cookie.getName : " + cookie.getName());
            if (cookie.getName().equals("Authorization")) {
                authorization = cookie.getValue();
            }
        }

        // Authorization 쿠키가 없으면 요청을 필터 체인으로 전달
        if (authorization == null) {
            System.out.println("token null");
            filterChain.doFilter(request, response);
            return;
        }

        // JWT 토큰이 만료되었는지 확인(만료:true, 미만료: false)
        String token = authorization;

        if (jwtUtil.isExpired(token)) {
            System.out.println("token expired");
            filterChain.doFilter(request, response);

            return;
        }

        // JWT 토큰에서 사용자명과 권한 추출
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        // UserDTO와 CustomOAuth2User 객체를 생성하여 인증 정보를 설정
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setRole(role);

        CustomOAuth2User customOAuth2User = new CustomOAuth2User(userDTO);

        // UsernamePasswordAuthenticationToken을 생성하여 인증 정보 설정
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(customOAuth2User, null, customOAuth2User.getAuthorities());

        // Spring Security의 SecurityContext에 인증 정보를 설정
        SecurityContextHolder.getContext().setAuthentication(authToken);

        // 요청을 필터 체인으로 전달
        filterChain.doFilter(request, response);
    }
}
