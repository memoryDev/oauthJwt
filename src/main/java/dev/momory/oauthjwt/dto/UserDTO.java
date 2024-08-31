package dev.momory.oauthjwt.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보를 저장하는 DTO 클래스
 * 사용자에 대한 기본적인 정보를 담기 위해 사용
 */
@Getter
@Setter
public class UserDTO {

    // 사용자 역할
    private String role;

    // 사용자 실명
    private String name;

    // 사용자명
    private String username;
}
