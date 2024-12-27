package com.ohgiraffers.semiproject.manager.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.manager.model.dto.*;
import com.ohgiraffers.semiproject.manager.model.service.ManagerApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ManagerApprovalAPIController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ManagerApprovalService managerApprovalService;

    @GetMapping("/api/approval-list")
    public ResponseEntity<ApprovalResponse> getApprovalList() {

        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String code = userInfo.getCode();

        List<CacPaymentDTO> cacPaymentList = managerApprovalService.getCacPaymentList(code);
        List<OverTimeDTO> overTimeList = managerApprovalService.getOverTimeList(code);
        List<RetirementDTO> retirementList = managerApprovalService.getRetirement(code);
        List<VacPaymentDTO> vacPaymentDTOList = managerApprovalService.getVacPayment(code);

        ApprovalResponse response = new ApprovalResponse();

        response.setCacPaymentList(cacPaymentList);
        response.setOverTimeList(overTimeList);
        response.setRetirementList(retirementList);
        response.setVacPaymentList(vacPaymentDTOList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/api/approvalUser")
    public ResponseEntity<ApprovalUserDTO> approvalUser() {
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();
        String name = userInfo.getName();
        String deptCode = userInfo.getDepartment();

        ApprovalUserDTO response = new ApprovalUserDTO(code, name, deptCode);

        return ResponseEntity.ok(response);
    }

}
