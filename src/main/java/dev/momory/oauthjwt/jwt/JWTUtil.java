package dev.momory.oauthjwt.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 관련 작업을 처리하는 유틸리티 클래스
 * JWT 토큰의 생성, 파싱, 검증 등을 수행함
 */
@Component
public class JWTUtil {

    private SecretKey secretKey; // JWT서명에 사용되는 비밀키

    /**
     * JWTUtil 생성자
     * @param secret JWT 서명에 사용할 비밀키를 가져옴
     */
    public JWTUtil(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(
                secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm()
        );
    }

    /**
     * JWT 토큰에서 사용자명을 추출
     * @param token JWT 토큰
     * @return JWT 토큰에서 추출한 사용자명
     */
    public String getUsername(String token) {
        return Jwts.parser()
                .verifyWith(secretKey) // 비밀 키로 JWT 서명 검증
                .build()
                .parseSignedClaims(token) // JWT 파싱하여 클레임을 추출
                .getPayload()
                .get("username", String.class); // "username" 클레임에서 사용자 명을 추출
    }

    /**
     * JWT 토큰에서 권한(role)를 추출
     * @param token JWT 토큰
     * @return JWT 토큰에서 추출한 권한
     */
    public String getRole(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .get("role", String.class);
    }

    /**
     * JWT 토큰의 만료 여부를 확인합니다.
     * @param token JWT 토큰
     * @return 토큰이 만료되었으면 true, 그렇지 않으면 false
     */
    public Boolean isExpired(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .before(new Date()); // 현재 시간과 토큰 만료 시간을 비교
    }

    /**
     * JWT 토큰을 생성합니다.
     * @param username 사용자명
     * @param role 사용자 권한
     * @param expiredMs 토큰 만료 시간(밀리초)
     * @return 생성된 JWT 토큰
     */
    public String createJwt(String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("username", username) // JWT 클레임에 사용자명 추가
                .claim("role", role) // JWT 클레임에 권한 추가
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 토큰 만료 시간
                .signWith(secretKey) // 비밀 키로 서명
                .compact(); // JWT 토큰 생성
    }
}
