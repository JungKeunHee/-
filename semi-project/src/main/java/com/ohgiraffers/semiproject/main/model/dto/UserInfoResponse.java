package com.ohgiraffers.semiproject.main.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {

    private String code; // 사용자 코드
    private String profilePictureUrl; // 프로필 이미지 URL
    private String name; // 사용자 이름
    private String department; // 부서명 추가
    private String position; // 직급 추가
    private String pass; // 사용자 비밀번호
}

