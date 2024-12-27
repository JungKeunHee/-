package com.ohgiraffers.semiproject.home.controller;

import com.ohgiraffers.semiproject.home.model.dto.NoCheckDTO;
import com.ohgiraffers.semiproject.home.model.dto.NoSearchDTO;
import com.ohgiraffers.semiproject.home.model.service.NoCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home/*")
public class NoCheckController {

    @Autowired
    private NoCheckService noCheckService;

    // 사원번호 찾기
    @PostMapping("no-check")
    public String noCheck(@ModelAttribute NoSearchDTO noSearchDTO, Model model) {

        NoCheckDTO noCheck = noCheckService.noCheck(noSearchDTO);

        if (noCheck == null) {
            model.addAttribute("errorMessage", "🚫일치하는 정보가 없습니다. 다시 입력해주세요🚫");
            return "home/no-search";
        } else {
            model.addAttribute("noCheck", noCheck);
        }

        return "home/no-check";
    }
}
