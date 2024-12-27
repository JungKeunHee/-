package com.ohgiraffers.semiproject.home.controller;

import com.ohgiraffers.semiproject.home.auth.model.service.MemberService;
import com.ohgiraffers.semiproject.home.model.dto.SignupDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    // 시간상 필드주입
    @Autowired
    private MemberService memberService;

    @GetMapping("/user/signup")
    public void signupPage(){}

    @PostMapping("/user/signup")
    public ModelAndView signup(@ModelAttribute SignupDTO signupDTO, ModelAndView mv) {

        // 이미지 경로를 추가
        String imagePath = "/img/profile/" + signupDTO.getImage(); // 예시로 사용자 이름을 파일명으로 사용
        signupDTO.setImage(imagePath); // SignupDTO에 이미지 경로를 추가하는 메서드 필요

        Integer result = memberService.regist(signupDTO);

        String message = null;

        if (result == null) {
            message = "중복 된 회원이 존재합니다.";
        } else if (result == 0) {
            message = "서버 내부에서 오류가 발생했습니다.";
            mv.setViewName("home");
        } else if (result >= 1) {
            message = "회원 가입이 완료되었습니다.";
            mv.setViewName("home");
        }

        mv.addObject("message", message);

        return mv;
    }

}
