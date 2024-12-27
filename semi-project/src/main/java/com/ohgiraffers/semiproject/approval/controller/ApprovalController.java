package com.ohgiraffers.semiproject.approval.controller;

import com.ohgiraffers.semiproject.approval.model.service.ApprovalService;
import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.manager.model.dto.*;
import com.ohgiraffers.semiproject.manager.model.service.ManagerApprovalService;
import com.ohgiraffers.semiproject.schedule.model.dto.VacationDTO;
import com.ohgiraffers.semiproject.schedule.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalTime;
import java.util.List;

@Controller
public class ApprovalController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ApprovalService approvalService;

    @Autowired
    private ManagerApprovalService managerApprovalService;

    @Autowired
    private ScheduleService scheduleService;

    // 결재 페이지로 이동
    @GetMapping("/sidemenu/approval")
    public String approval() {
        return "sidemenu/approval/approval";
    }


    @GetMapping("/approval/createVacation-form")
    public String vacationForm(Model model){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        // 기안자 연차 및 휴가 갯수 정보 가져오기
        VacationDTO vacationDTO = scheduleService.getVacation(code);

        // 휴가 갯수
        model.addAttribute("vacationCountInfo", vacationDTO.getVacation());

        // 연차 갯수
        model.addAttribute("yearLeaveCountInfo", vacationDTO.getAnnualLeave());

        return  "sidemenu/approval/createVacation-form";
    }

    @GetMapping("/approval/createCac-form")
    public String cacForm(){
        return  "sidemenu/approval/createCac-form";
    }

    @GetMapping("/approval/createOvertime-form")
    public String overTimeForm(){
        return "sidemenu/approval/createOverTime-form";
    }

    @GetMapping("/approval/createRetirement-form")
    public String retirement(){

        return "sidemenu/approval/createRetirement-form";
    }

    @PostMapping("/myVacationFormRequest")
    public String vacationFormRequest(@ModelAttribute VacPaymentDTO vacPaymentDTO){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        vacPaymentDTO.setEmpCode(code);
        vacPaymentDTO.setOwnerCode("025"); // 대표 사번
        vacPaymentDTO.setProgressCode("SU1");

        approvalService.insertVacForm(vacPaymentDTO);

        return "sidemenu/approval/approval";
    }

    @PostMapping("/myCacFormRequest")
    public String cacFormRequest(@ModelAttribute CacPaymentDTO cacPaymentDTO){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        cacPaymentDTO.setEmpCode(code);
        cacPaymentDTO.setOwnerCode("025"); // 대표 사번
        cacPaymentDTO.setProgressCode("SU1");

        approvalService.insertCacForm(cacPaymentDTO);

        return "sidemenu/approval/approval";
    }

    @PostMapping("/myOverTimeFormRequest")
    public String overTimeFormRequest(@ModelAttribute OverTimeDTO overTimeDTO,
                                      @RequestParam("overTime") String overTimeString) {
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String code = userInfo.getCode();

        overTimeDTO.setEmpCode(code);
        overTimeDTO.setOwnerCode("025"); // 대표 사번
        overTimeDTO.setProgressCode("SU1");

        // String을 LocalTime으로 변환
        if (overTimeString != null && !overTimeString.isEmpty()) {
            overTimeDTO.setOverTime(LocalTime.parse(overTimeString));
        } else {
            overTimeDTO.setOverTime(null); // null 처리
        }

        approvalService.insertOverTimeForm(overTimeDTO);

        return "sidemenu/approval/approval";
    }

    @PostMapping("/myRetirementFormRequest")
    public String retirementFormRequest(@ModelAttribute RetirementDTO retirementDTO){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        retirementDTO.setEmpCode(code);
        retirementDTO.setOwnerCode("025"); // 대표 사번
        retirementDTO.setProgressCode("SU1");

        approvalService.insertRetireMentForm(retirementDTO);

        return "sidemenu/approval/approval";
    }


    @GetMapping("/api/my-approval-list")
    public ResponseEntity<ApprovalResponse> getApprovalList() {

        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String code = userInfo.getCode();

        List<CacPaymentDTO> cacPaymentList = approvalService.getMyCacPaymentList(code);
        List<OverTimeDTO> overTimeList = approvalService.getMyOverTimeList(code);
        List<RetirementDTO> retirementList = approvalService.getMyRetirement(code);
        List<VacPaymentDTO> vacPaymentDTOList = approvalService.getMyVacPayment(code);

        ApprovalResponse response = new ApprovalResponse();

        response.setCacPaymentList(cacPaymentList);
        response.setOverTimeList(overTimeList);
        response.setRetirementList(retirementList);
        response.setVacPaymentList(vacPaymentDTOList);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/myVacRequest")
    public String approvalVacRequest(@RequestParam("no") String documentNo, Model model) {

        VacPaymentDTO response = managerApprovalService.getVacRequest(documentNo);

        VacPaymentDTO updatedProcess = managerApprovalService.getVacationDetails(documentNo);

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        // 관리자 or 대표 승인 값 가져오기
        model.addAttribute("adminAccept", updatedProcess.getManagerAccept());
        model.addAttribute("ownerAccept", updatedProcess.getPresidentAccept());

        // no 값 넘기기
        model.addAttribute("no", documentNo);

        // 포맷팅된 날짜를 모델에 추가
        model.addAttribute("formattedDeadLineDate", response.getFormattedDeadLineDate());
        model.addAttribute("formattedVacStartDate", response.getFormattedVacStartDate());
        model.addAttribute("formattedVacEndDate", response.getFormattedVacEndDate());

        model.addAttribute("vacRequestInfo", response);

        // 현재 로그인한 사번 코드
        model.addAttribute("currentEmpCode", code);

        // 관리자 사번 코드
        model.addAttribute("adminCode", response.getAdminCode());
        model.addAttribute("ownerCode", response.getOwnerCode());

        return "sidemenu/approval/myVacRequest";
    }

    @GetMapping("/myCacRequest")
    public String approvalCacRequest(@RequestParam("no") String documentNo, Model model) {

        CacPaymentDTO response = managerApprovalService.getCacRequest(documentNo);

        CacPaymentDTO updatedProcess = managerApprovalService.getCacDetails(documentNo);

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        // 관리자 or 대표 승인 값 가져오기
        model.addAttribute("adminAccept", updatedProcess.getManagerAccept());
        model.addAttribute("ownerAccept", updatedProcess.getPresidentAccept());

        // no 값 넘기기
        model.addAttribute("no", documentNo);

        // 포맷팅된 날짜를 모델에 추가
        model.addAttribute("formattedDeadLineDate", response.getFormattedDeadLineDate());
        model.addAttribute("formattedVacStartDate", response.getFormattedCacStartDate());
        model.addAttribute("formattedVacEndDate", response.getFormattedCacEndDate());

        model.addAttribute("cacRequestInfo", response);

        // 현재 로그인한 사번 코드
        model.addAttribute("currentEmpCode", code);

        // 관리자 사번 코드
        model.addAttribute("adminCode", response.getAdminCode());
        model.addAttribute("ownerCode", response.getOwnerCode());

        // 계좌번호 가져오기
        model.addAttribute("accountNum", response.getAccountNum());

        return "sidemenu/approval/myCacRequest";
    }

    @GetMapping("/myOverTimeRequest")
    public String approvalOverTimeRequest(@RequestParam("no") String documentNo, Model model) {

        OverTimeDTO response = managerApprovalService.getOverTimeRequest(documentNo);

        OverTimeDTO updatedProcess = managerApprovalService.getOverTimeDetails(documentNo);

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        // 관리자 or 대표 승인 값 가져오기
        model.addAttribute("adminAccept", updatedProcess.getManagerAccept());
        model.addAttribute("ownerAccept", updatedProcess.getPresidentAccept());

        // 포맷팅된 날짜를 모델에 추가
        model.addAttribute("formattedDeadLineDate", response.getFormattedDeadLineDate());

        // no 값 넘기기
        model.addAttribute("no", documentNo);

        model.addAttribute("overTimeRequestInfo", response);

        // 연장근무 시간 넘기기
        model.addAttribute("overTime", response.getOverTime());

        // 현재 로그인한 사번 코드
        model.addAttribute("currentEmpCode", code);

        // 관리자 사번 코드
        model.addAttribute("adminCode", response.getAdminCode());
        model.addAttribute("ownerCode", response.getOwnerCode());

        return "sidemenu/approval/myOverTimeRequest";
    }

    @GetMapping("/myRetireMentRequest")
    public String approvalRetireMentRequest(@RequestParam("no") String documentNo, Model model) {

        RetirementDTO response = managerApprovalService.getRetirementRequest(documentNo);

        RetirementDTO updatedProcess = managerApprovalService.getRetirementDetails(documentNo);

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        // 관리자 or 대표 승인 값 가져오기
        model.addAttribute("adminAccept", updatedProcess.getManagerAccept());
        model.addAttribute("ownerAccept", updatedProcess.getPresidentAccept());

        // no 값 넘기기
        model.addAttribute("no", documentNo);

        // 포맷팅된 날짜를 모델에 추가
        model.addAttribute("formattedDeadLineDate", response.getFormattedDeadLineDate());
        model.addAttribute("formattedRetireDate", response.getFormattedCacStartDate());

        model.addAttribute("retirementRequestInfo", response);

        // 현재 로그인한 사번 코드
        model.addAttribute("currentEmpCode", code);

        // 관리자 사번 코드
        model.addAttribute("adminCode", response.getAdminCode());
        model.addAttribute("ownerCode", response.getOwnerCode());

        return "sidemenu/approval/myRetireMentRequest";
    }

    @PostMapping("/deleteCacProcess")
    public String deleteCacProcess(@RequestParam("documentNo") int no){

        System.out.println("no = " + no);

        approvalService.deleteCacProcess(no);

        return "sidemenu/approval/approval";
    }

    @PostMapping("/deleteOverTimeProcess")
    public String deleteOverTimeProcess(@RequestParam("documentNo") int no){

        System.out.println("no = " + no);

        approvalService.deleteOverTimeProcess(no);

        return "sidemenu/approval/approval";
    }

    @PostMapping("/deleteRetirementProcess")
    public String deleteRetirementProcess(@RequestParam("documentNo") int no){

        System.out.println("no = " + no);

        approvalService.deleteRetirementProcess(no);

        return "sidemenu/approval/approval";
    }

    @PostMapping("/deleteVacationProcess")
    public String deleteVacationProcess(@RequestParam("documentNo") int no){

        System.out.println("no = " + no);

        approvalService.deleteVacationProcess(no);

        return "sidemenu/approval/approval";
    }

}

