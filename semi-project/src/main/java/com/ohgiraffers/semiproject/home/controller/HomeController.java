package com.ohgiraffers.semiproject.home.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("home/no-search")
    public String noSearch(){
        return "/home/no-search";
    }

    @GetMapping("home/pass-search")
    public String passSearch(){
        return "/home/pass-search";
    }
}
