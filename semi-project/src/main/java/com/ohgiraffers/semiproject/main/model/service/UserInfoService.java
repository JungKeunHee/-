package com.ohgiraffers.semiproject.main.model.service;

import com.ohgiraffers.semiproject.home.auth.model.dto.AuthDetailes;
import com.ohgiraffers.semiproject.home.model.dto.LoginUserDTO;
import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService {

    public UserInfoResponse getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            AuthDetailes userDetails = (AuthDetailes) authentication.getPrincipal();

            // 사용자 정보 추출
            String code = userDetails.getUsername();
            LoginUserDTO loginUserDTO = userDetails.getLoginUserDTO();
            String profilePictureUrl = loginUserDTO.getImage(); // 프로필 이미지 URL 가져오기
            String name = loginUserDTO.getName(); // 이름 가져오기
            String department = loginUserDTO.getDeptCode(); // 부서명 가져오기
            String position = loginUserDTO.getJobCode(); // 직급 가져오기
            String pass = loginUserDTO.getPass(); // 비밀번호 가져오기

            return new UserInfoResponse(code, profilePictureUrl, name, department, position, pass);
        }
        return null; // 또는 적절한 에러 처리
    }
}
