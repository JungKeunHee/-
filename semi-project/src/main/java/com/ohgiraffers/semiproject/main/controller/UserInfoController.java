package com.ohgiraffers.semiproject.main.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @GetMapping("/user/info")
    public ResponseEntity<UserInfoResponse> getUserInfo() {

        UserInfoResponse info = userInfoService.getUserInfo();

        return ResponseEntity.ok(info);
    }
}
