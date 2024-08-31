package dev.momory.oauthjwt.dto;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * OAuth2 사용자 정보를 커스터마이징하기 위한 클래스
 * Spring Security의 OAuth2User 인터페이스를 구현하여 상요자 정보를 확장합니다.
 */
public class CustomOAuth2User implements OAuth2User {

    // 사용자 정보가 담긴 UserDTO
    private final UserDTO userDTO;

    public CustomOAuth2User(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    /**
     * OAuth2 사용자 속성 정보를 반환합니다.
     * (사용하지 않는 메서드)
     * @return 빈맵
     */
    @Override
    public Map<String, Object> getAttributes() {
        return Map.of();
    }


    /**
     * 사용자의 권한 목록을 반환합니다.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userDTO.getRole();
            }
        });

        return authorities;
    }

    /**
     * 사용자의 이름을 반환합니다.
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        return userDTO.getName();
    }

    /**
     * 사용자의 사용자명을 반환합니다.
     * @return사용자명
     */
    public String getUsername() {
        return userDTO.getUsername();
    }
}
