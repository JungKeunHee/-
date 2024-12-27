package com.ohgiraffers.semiproject.schedule.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.schedule.model.dto.ScheduleDTO;
import com.ohgiraffers.semiproject.schedule.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Controller
public class ScheduleController {

    @Autowired
    private UserInfoService userInfoService;

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 근태관리 페이지 이동
    @GetMapping("/sidemenu/schedule")
    public String schedule() {
        return "sidemenu/schedule/schedule";
    }

    // 근태 수정 페이지 이동
    @GetMapping("/attendance-request")
    public String attendance(){
        return "/sidemenu/schedule/attendance-request";
    }

    @PostMapping("/attendance-request")
    public String attendanceRequest(@RequestParam("workDate") String workDateStr,
                                    @RequestParam("modify_time") String modifyTimeStr,
                                    @ModelAttribute ScheduleDTO attendanceModifyInfo) {

        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String userCode = userInfo.getCode(); // 사번 가져오기

        attendanceModifyInfo.setEmpCode(userCode);

        // workDateStr을 LocalDate로 변환
        LocalDate workDate = LocalDate.parse(workDateStr);

        // modify_timeStr을 LocalTime으로 변환 (초를 0으로 설정)
        LocalTime localTime = LocalTime.parse(modifyTimeStr);

        // workDate와 localTime을 결합하여 LocalDateTime 생성
        LocalDateTime modifyDateTime = LocalDateTime.of(workDate, localTime);

        // ScheduleDTO에 modifyTime 설정
        attendanceModifyInfo.setModifyTime(modifyDateTime);

        scheduleService.modifyAttendanceRequest(attendanceModifyInfo);

        return "/sidemenu/schedule/attendance-request";
    }

}