package dev.momory.oauthjwt.dto;

import java.util.Map;

/**
 * Google OAuth2 제공자로부터 받은 사용자 정보를 처리하는 클래스
 * Google의 OAuth2 응답 데이터를 'OAuth2Response' 인터페이스에 맞게 변환합니다ㅣ.
 */
public class GoogleResponse implements OAuth2Response{

    private final Map<String ,Object> attribute;

    /**
     * GoogleResponse 생성자
     * @param attribute Google에서 받은 사용자 정보 속성 맵
     */
    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    /**
     * 제공자 이름을 반환합니다.
     * @return "google"
     */
    @Override
    public String getProvider() {
        return "google";
    }

    /**
     * 제공자가 발급한 고유 사용자 ID를 반환합니다.
     * Google의 경우 사용자 ID는 "sub"필드에 포함됩니다.
     * @return Google에서 발급한 사용자 ID
     */
    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
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
