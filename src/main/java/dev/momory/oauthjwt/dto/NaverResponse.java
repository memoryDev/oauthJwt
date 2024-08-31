package dev.momory.oauthjwt.dto;

import java.util.Map;

/**
 * Naver OAuth2 제공자로부터 받은 사용자 정보를 처리하는 클래스
 * Naver OAuth2 응답 데이터를 'OAuth2Response' 인터페이스 맞게 변환합니다.
 */
public class NaverResponse implements OAuth2Response{

    // Naver로부터 받은 사용자 정보 속성들을 저장하는 맵
    private final Map<String ,Object> attribute;

    /**
     * NaverResponse 생성자
     * @param attribute Naver에서 받은 사용자 정보 속성 맵
     */
    public NaverResponse(Map<String, Object> attribute) {
        // Naver의 응답 구조에서 실제 사용자 정보는 "response"키 아래에 있음
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    /**
     * 제공자 이름을 반환합니다.
     * @return "naver"
     */
    @Override
    public String getProvider() {
        return "naver";
    }

    /**
     * 제공자가 발급한 고유 사용자 ID를 반환합니다.
     * Naver의 경우 사용자 ID는 "id" 필드에 포함됩니다.
     * @return Naver에서 발급한 사용자 ID
     */
    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    /**
     * 사용자의 이메일 주소를 반환합니다.
     * @return 사용자 이메일
     */
    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    /**
     * 사용자의 실명을 반환합니다.
     * @return 사용자 이름
     */
    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
