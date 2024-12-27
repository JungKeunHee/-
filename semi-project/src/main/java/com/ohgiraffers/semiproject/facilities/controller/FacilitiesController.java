package com.ohgiraffers.semiproject.facilities.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FacilitiesController {

    // 시설관리 페이지로 이동
    @GetMapping("/sidemenu/facilities")
    public String facilities() {
        return "sidemenu/facilities/facilities";
    }

    @GetMapping("/sidemenu/facilities/facilities")
    public String facilitiesSelect(Model model) {


        return "";
    }
}
