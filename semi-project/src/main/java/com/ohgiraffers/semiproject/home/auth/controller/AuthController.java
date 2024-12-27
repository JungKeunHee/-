package com.ohgiraffers.semiproject.home.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {

    @GetMapping("/home")
    public void homePage() {
    }

    @GetMapping("/home/fail")
    public ModelAndView loginFail(@RequestParam String message, ModelAndView mv) {
        // 실패 시 핸들러에서 쿼리스트링으로 보내주는 errorMessage
        mv.addObject("message", message);
        mv.setViewName("home/fail");

        return mv;
    }
}
