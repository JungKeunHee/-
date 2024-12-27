package com.ohgiraffers.semiproject.manager.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.manager.model.dto.ManagerScheduleDTO;
import com.ohgiraffers.semiproject.manager.model.service.ManagerScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class ManagerAttendanceAPIController {

    private final UserInfoService userInfoService;
    private final ManagerScheduleService managerScheduleService;

    @Autowired
    public ManagerAttendanceAPIController(UserInfoService userInfoService,
                                          ManagerScheduleService managerScheduleService){
        this.userInfoService = userInfoService;
        this.managerScheduleService = managerScheduleService;
    }

    @GetMapping("/attendanceManagement")
    public ResponseEntity<List<ManagerScheduleDTO>> getAttendanceRequest(){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        List<ManagerScheduleDTO> list = managerScheduleService.getAttendanceRequest(code);

        // ResponseEntity로 리스트 반환
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/adminScheduleProcess")
    public String adminScheduleProcess(@ModelAttribute ManagerScheduleDTO parameter) {

        System.out.println("parameter = " + parameter);

        // division 필드 값에 따라 다른 메소드 실행
        if ("출근".equals(parameter.getDivision())) {
            // 출근일 때 실행할 메소드
            managerScheduleService.modifyStartTime(parameter);
        } else if ("퇴근".equals(parameter.getDivision())) {
            // 퇴근일 때 실행할 메소드
            managerScheduleService.modifyEndTime(parameter);
        } else if ("SU4".equals(parameter.getProgressCode())){
            // 반려일 때 실행할 메소드
            managerScheduleService.modifyProgressCode(parameter);
        }
        return "sidemenu/manager/scheduleManagement";
    }

}
