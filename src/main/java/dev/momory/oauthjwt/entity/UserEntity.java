package dev.momory.oauthjwt.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자 정보를 DB에 저장하기 위한 JPA 엔티티 클래스
 */
@Entity
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String name;

    private String email;

    private String role;


}
