package dev.momory.oauthjwt.service;

import dev.momory.oauthjwt.dto.*;
import dev.momory.oauthjwt.entity.UserEntity;
import dev.momory.oauthjwt.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

/**
 * OAuth2 사용자 정보를 처리하는 서비스 클래스
 * 사용자 정보를 가져와서 DB에 저장하거나 수정함
 */
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * OAuth2 사용자 정보를 도르합니다.
     * @param userRequest OAuth2 사용자 요청
     * @return OAuth2User
     * @throws OAuth2AuthenticationException 인증 예외
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService를 통해 사용자 정보를 조회
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println("===== oAuth2User =====");
        System.out.println(oAuth2User);

        // 클라이언트 등록 ID를 통해 제공자를 식별합니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        // 제공자에 따라 OAuth2Response 객체 생성
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        } else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        } else {
            // 지원하지 않는 제공자일 경우 null 반환
            return null;
        }

        // 사용자명을 제공자와 제공자 ID로 생성
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        // DB에서 사용자 정보 조회
        UserEntity existData = userRepository.findByUsername(username);

        if (existData == null) {
            // DB에 사용자 정보가 없으면 새 사용자 엔티티 생성하고 저장
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(username);
            userEntity.setEmail(oAuth2Response.getEmail());
            userEntity.setName(oAuth2Response.getName());
            userEntity.setRole("ROLE_USER");

            userRepository.save(userEntity);

            // 새로 생성된 사용자 정보를 UserDTO로 변환하여 CustomOAuth2User 객체를 반환합니다.
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(username);
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole("ROLE_USER");

            return new CustomOAuth2User(userDTO);

        } else {

            // DB에 사용자 정보가 있으면 정보를 수정합니다.
            existData.setEmail(oAuth2Response.getEmail());
            existData.setName(oAuth2Response.getName());

            userRepository.save(existData);

            // 업데이트된 사용자 정보를 UserDTO로 변환하여 CustomOAuth2User 객체를 반환
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(existData.getUsername());
            userDTO.setName(oAuth2Response.getName());
            userDTO.setRole(existData.getRole());

            return new CustomOAuth2User(userDTO);

        }



    }
}
