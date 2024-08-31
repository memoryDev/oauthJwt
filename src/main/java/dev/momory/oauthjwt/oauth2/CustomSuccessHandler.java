package dev.momory.oauthjwt.oauth2;

import dev.momory.oauthjwt.dto.CustomOAuth2User;
import dev.momory.oauthjwt.jwt.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

/**
 * 인증 성공 후 후속 작업을 처리하는 핸들러 클래스
 * JWT 토큰을 생성하고, 쿠키로 응답에 추가하며, 리다레이션을 수행
 */
@Component
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil; // JWT 유틸리티 클래스

    public CustomSuccessHandler(JWTUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    /**
     * 인증 성공 시 호출되는 메서드
     * @param request HTTP 요청
     * @param response HTTPP 응답
     * @param authentication 인증 정보
     * @throws IOException 입출력 관련 예외
     * @throws ServletException 서블릿 관련 예외
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        // 인증 정보에서 사용자 세부정보를 가져옵니다.
        CustomOAuth2User customUserDetail = (CustomOAuth2User) authentication.getPrincipal();
        String username = customUserDetail.getUsername();

        // 사용자 권한을 가져와서 역할을 추출
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // JWT 토큰을 생성합니다.
        String token = jwtUtil.createJwt(username, role, 60 * 60 * 60L);

        // JWT 토큰을 "Authorization" 쿠키로 응답에 추가합니다.
        response.addCookie(createCookie("Authorization", token));

        // 인증 성공 후 리다이렉션 수행
        response.sendRedirect("http://localhost:9000");

    }

    /**
     * 쿠키를 생성
     * @param key 쿠키 이름
     * @param value 쿠키 값
     * @return 생성된 쿠키
     */
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        //cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
