package com.ohgiraffers.semiproject.manager.controller;

import com.ohgiraffers.semiproject.approval.model.service.ApprovalService;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeJoinListDTO;
import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.OverTimeDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.service.ManagerApprovalService;
import com.ohgiraffers.semiproject.schedule.model.dto.VacationDTO;
import com.ohgiraffers.semiproject.schedule.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

@Controller
public class ManagerApprovalController {

    private final ManagerApprovalService managerApprovalService;
    private final ApprovalService approvalService;
    private final UserInfoService userInfoService;
    private final ScheduleService scheduleService;

    @Autowired
    public ManagerApprovalController(ManagerApprovalService managerApprovalService,
                                     ApprovalService approvalService, UserInfoService userInfoService,
                                     ScheduleService scheduleService){
        this.managerApprovalService = managerApprovalService;
        this.approvalService = approvalService;
        this.userInfoService = userInfoService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/approval/vacation-form")
    public String vacationForm(Model model){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        // 기안자 연차 및 휴가 갯수 정보 가져오기
        VacationDTO vacationDTO = scheduleService.getVacation(code);

        // 휴가 갯수
        model.addAttribute("vacationCountInfo", vacationDTO.getVacation());

        // 연차 갯수
        model.addAttribute("yearLeaveCountInfo", vacationDTO.getAnnualLeave());

        return  "sidemenu/approval/vacation-form";
    }

    @GetMapping("/approval/cac-form")
    public String cacForm(){
        return  "sidemenu/approval/cac-form";
    }

    @GetMapping("/approval/overtime-form")
    public String overTimeForm(){
        return "sidemenu/approval/overTime-form";
    }

    @GetMapping("/approval/retirement-form")
    public String retirement(){

        return "sidemenu/approval/retirement-form";
    }

    @PostMapping("/vacationFormRequest")
    public String vacationFormRequest(@ModelAttribute VacPaymentDTO vacPaymentDTO){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        vacPaymentDTO.setEmpCode(code);
        vacPaymentDTO.setOwnerCode("025"); // 대표 사번
        vacPaymentDTO.setProgressCode("SU1");

        approvalService.insertVacForm(vacPaymentDTO);

        return "/sidemenu/manager/approvalBox";
    }

    @PostMapping("/cacFormRequest")
    public String cacFormRequest(@ModelAttribute CacPaymentDTO cacPaymentDTO){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        cacPaymentDTO.setEmpCode(code);
        cacPaymentDTO.setOwnerCode("025"); // 대표 사번
        cacPaymentDTO.setProgressCode("SU1");

        approvalService.insertCacForm(cacPaymentDTO);

        return "/sidemenu/manager/approvalBox";
    }

    @PostMapping("/overTimeFormRequest")
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

        return "/sidemenu/manager/approvalBox";
    }

    @PostMapping("/retirementFormRequest")
    public String retirementFormRequest(@ModelAttribute RetirementDTO retirementDTO){

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        retirementDTO.setEmpCode(code);
        retirementDTO.setOwnerCode("025"); // 대표 사번
        retirementDTO.setProgressCode("SU1");

        approvalService.insertRetireMentForm(retirementDTO);

        return "/sidemenu/manager/approvalBox";
    }

    @GetMapping("/readVacRequest")
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

        return "sidemenu/approval/readVacRequest";
    }

    @GetMapping("/readCacRequest")
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

        return "sidemenu/approval/readCacRequest";
    }

    @GetMapping("/readOverTimeRequest")
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

        return "sidemenu/approval/readOverTimeRequest";
    }

    @GetMapping("/readRetireMentRequest")
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

        return "sidemenu/approval/readRetirementRequest";
    }

    @PostMapping("/cacProcess")
    public String cacProcess(@ModelAttribute CacPaymentDTO process) {

        // managerAccept 값에 따라 progressCode 설정
        if (process.getManagerAccept() != null) {
            if (process.getManagerAccept().equals("/img/icon/Stamp.png")) {

                process.setProgressCode("SU2"); // 진행중으로 변경
            } else if (process.getManagerAccept().equals("반려")) {
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }

        // presidenAccept 값에 따라 progressCode 설정
        if (process.getPresidentAccept() != null) {
            if (process.getPresidentAccept().equals("/img/icon/Stamp.png")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU3"); // SU3으로 설정
            } else if (process.getPresidentAccept().equals("반려")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }

        // 승인 처리
        managerApprovalService.cacProcess(process);

        return "redirect:/readCacRequest?no=" + process.getDocumentNo();
    }

    @PostMapping("/overTimeProcess")
    public String overTimeProcess(@ModelAttribute OverTimeDTO process) {

        // managerAccept 값에 따라 progressCode 설정
        if (process.getManagerAccept() != null) {
            if (process.getManagerAccept().equals("/img/icon/Stamp.png")) {

                process.setProgressCode("SU2"); // 진행중으로 변경
            } else if (process.getManagerAccept().equals("반려")) {
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }

        // presidenAccept 값에 따라 progressCode 설정
        if (process.getPresidentAccept() != null) {
            if (process.getPresidentAccept().equals("/img/icon/Stamp.png")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU3"); // SU3으로 설정
            } else if (process.getPresidentAccept().equals("반려")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }

        // 승인 처리
        managerApprovalService.overTimeProcess(process);

        return "redirect:/readOverTimeRequest?no=" + process.getDocumentNo();
    }

    @PostMapping("/retirementProcess")
    public String retirementProcess(@ModelAttribute RetirementDTO process) {

        // managerAccept 값에 따라 progressCode 설정
        if (process.getManagerAccept() != null) {
            if (process.getManagerAccept().equals("/img/icon/Stamp.png")) {

                process.setProgressCode("SU2"); // 진행중으로 변경
            } else if (process.getManagerAccept().equals("반려")) {
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }

        // presidenAccept 값에 따라 progressCode 설정
        if (process.getPresidentAccept() != null) {
            if (process.getPresidentAccept().equals("/img/icon/Stamp.png")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU3"); // SU3으로 설정
            } else if (process.getPresidentAccept().equals("반려")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }
        
        // 승인 처리
        managerApprovalService.retirementProcess(process);

        return "redirect:/readRetireMentRequest?no=" + process.getDocumentNo();
    }

    @PostMapping("/vacationProcess")
    public String vacationProcess(@ModelAttribute VacPaymentDTO process) {

        // managerAccept 값에 따라 progressCode 설정
        if (process.getManagerAccept() != null) {
            if (process.getManagerAccept().equals("/img/icon/Stamp.png")) {

                process.setProgressCode("SU2"); // 진행중으로 변경
            } else if (process.getManagerAccept().equals("반려")) {
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }

        // presidenAccept 값에 따라 progressCode 설정
        if (process.getPresidentAccept() != null) {
            if (process.getPresidentAccept().equals("/img/icon/Stamp.png")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU3"); // SU3으로 설정
            } else if (process.getPresidentAccept().equals("반려")) {
                process.setManagerAccept("/img/icon/Stamp.png");
                process.setProgressCode("SU4"); // 반려로 설정
            }
        }


        // 승인 처리
        managerApprovalService.vacationProcess(process);

        // 기안자 신청서 시작날짜 마지막날짜 가져오기
        VacPaymentDTO response = managerApprovalService.getDate(process.getDocumentNo());

        // 기안자 휴가 정보 가져오기
        VacationDTO vacationDTO = scheduleService.getVacation(response.getEmpCode());

        if ((response.getType().equals("연차")) &&
                (response.getManagerAccept().equals("/img/icon/Stamp.png")) &&
                (response.getPresidentAccept().equals("/img/icon/Stamp.png"))) {

            String code = response.getEmpCode();

            int annualLeave = vacationDTO.getAnnualLeave();

            // 시작일과 종료일을 Date 객체로 변환
            Date vacStartDate = response.getVacStartDate(); // 예: Thu Dec 26 00:00:00 KST 2024
            Date vacEndDate = response.getVacEndDate(); // 예: Tue Dec 31 00:00:00 KST 2024

            // 날짜 포맷 설정
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            startCal.setTime(vacStartDate);
            endCal.setTime(vacEndDate);

            // 시작일과 종료일의 차이를 계산 (종료일 포함하지 않음)
            int daysBetween = (int) ((endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60 * 60 * 24));

            // 연차 차감
            annualLeave -= daysBetween + 1; // 시작일 포함

            // 업데이트된 연차 수를 저장
            vacationDTO.setAnnualLeave(annualLeave);
            scheduleService.updateVacation(code, vacationDTO);

        } else if ((response.getType().equals("휴가")) &&
                (response.getManagerAccept().equals("/img/icon/Stamp.png")) &&
                (response.getPresidentAccept().equals("/img/icon/Stamp.png"))) {

            String code = response.getEmpCode();

            int vacation = vacationDTO.getVacation();

            // 시작일과 종료일을 Date 객체로 변환
            Date vacStartDate = response.getVacStartDate(); // 예: Thu Dec 26 00:00:00 KST 2024
            Date vacEndDate = response.getVacEndDate(); // 예: Tue Dec 31 00:00:00 KST 2024

            // 날짜 포맷 설정
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            startCal.setTime(vacStartDate);
            endCal.setTime(vacEndDate);

            // 시작일과 종료일의 차이를 계산 (종료일 포함하지 않음)
            int daysBetween = (int) ((endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 60 * 60 * 24));

            // 휴가 차감
            vacation -= daysBetween + 1; // 시작일 포함

            // 업데이트된 연차 수를 저장
            vacationDTO.setVacation(vacation);
            scheduleService.updateVacation(code, vacationDTO);
        }

        return "redirect:/readVacRequest?no=" + process.getDocumentNo();
        }
}
