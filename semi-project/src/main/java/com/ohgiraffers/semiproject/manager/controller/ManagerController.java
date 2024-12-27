package com.ohgiraffers.semiproject.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagerController {

    // 관리자전용 페이지로 이동
    @GetMapping("/sidemenu/manager")
    public String manager() {
        return "sidemenu/manager/manager";
    }

    // 사원등록 페이지로 이동
    @GetMapping("sidemenu/employeeRegister")
    public String employeeRegister() {
        return "sidemenu/manager/employeeRegister";
    }

    // 직원관리 페이지로 이동
    @GetMapping("sidemenu/employeeManagement")
    public String employeeManagement() {
        return "sidemenu/manager/employeeManagement";
    }

    // 결재함 페이지로 이동
    @GetMapping("sidemenu/approvalBox")
    public String approvalBox() {
        return "sidemenu/manager/approvalBox";
    }

    // 근태수정 요청관리 페이지로 이동
    @GetMapping("/scheduleManagement")
    public String scheduleManagement(){
       return "sidemenu/manager/scheduleManagement";
    }

    // 연차 & 휴가 관리 페이지로 이동
    @GetMapping("/vacationManagement")
    public String vacationManagement(){
        return "sidemenu/manager/vacationManagement";
    }
}
