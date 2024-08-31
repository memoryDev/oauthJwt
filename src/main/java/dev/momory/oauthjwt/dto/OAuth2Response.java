package dev.momory.oauthjwt.dto;

/**
 * OAuth2 인증 제공자로부터 응답은 사용자 정보를 추상화한 인터페이스
 * 다양한 OAuth2 제공자(naver, google)로 부터 통일된 형식의 사용자 정보를 얻기 위해 사용됩니다.
 * */
public interface OAuth2Response {

    /**
     * 제공자 이름을 반환합니다(예: naver, google)
     * @return OAuth2 제공자의 이름
     */
    String getProvider();

    /**
     * 제공자가 발급한 고유 사용자 ID를 반환합니다.
     * @return 제공자로부터 발급받은 사용자 ID
     */
    String getProviderId();

    /**
     * 사용자의 이메일 주소를 반환합니다.
     * @return 사용자 이메일
     */
    String getEmail();

    /**
     * 사용자의 실명을 반환합니다.
     * @return 사용자 이름
     */
    String getName();


}
