package com.ohgiraffers.semiproject.manager.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.manager.model.dto.*;
import com.ohgiraffers.semiproject.manager.model.service.ManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManagerAPIController {

    private final UserInfoService userInfoService;
    private final ManagerService managerService;

    public ManagerAPIController(UserInfoService userInfoService,
                                ManagerService managerService){
        this.userInfoService = userInfoService;
        this.managerService = managerService;
    }

    @GetMapping("/api/manager")
    public ResponseEntity<ManagerDataListDTO> getData(){

        UserInfoResponse info = userInfoService.getUserInfo();

        String code = info.getCode();

        List<ManagerScheduleDTO> scheduleDTOList =  managerService.getScheduleList(code);
        List<CacPaymentDTO> cacPaymentList = managerService.getCacPaymentList(code);
        List<OverTimeDTO> overTimeList = managerService.getOverTimeList(code);
        List<RetirementDTO> retirementList = managerService.getRetirement(code);
        List<VacPaymentDTO> annualPaymentList = managerService.getAnnualList(code);
        List<VacPaymentDTO> vacationPaymentList = managerService.getVacationList(code);

        ManagerDataListDTO response = new ManagerDataListDTO();

        response.setScheduleList(scheduleDTOList);
        response.setCacPaymentList(cacPaymentList);
        response.setOverTimeList(overTimeList);
        response.setRetirementList(retirementList);
        response.setAnnualPaymentList(annualPaymentList);
        response.setVacationPaymentList(vacationPaymentList);

        return ResponseEntity.ok(response);
    }
}
