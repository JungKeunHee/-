package com.ohgiraffers.semiproject.schedule.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import com.ohgiraffers.semiproject.schedule.model.dto.*;
import com.ohgiraffers.semiproject.schedule.model.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/sidemenu/schedule/*")
public class ScheduleApiController {

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("checkin")
    public ResponseEntity<String> checkIn(@RequestBody CheckInRequestDTO checkInRequest) {
        // 요청에서 받은 날짜와 시간을 처리
        String date = checkInRequest.getDate(); // 예: "2024-12-11"
        String startTime = checkInRequest.getStartTime(); // 예: "09:00:00"

        // 날짜를 LocalDate로 변환
        LocalDate workDate;
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            workDate = LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("날짜 형식이 잘못되었습니다.");
        }

        // 시간을 LocalTime으로 변환
        LocalTime workStartTime;
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            workStartTime = LocalTime.parse(startTime, timeFormatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("시간 형식이 잘못되었습니다.");
        }

        // LocalDate와 LocalTime을 결합하여 LocalDateTime으로 변환
        LocalDateTime workStartDateTime = LocalDateTime.of(workDate, workStartTime);

        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setEmpCode(userCode);
            scheduleDTO.setWorkDate(Date.from(workStartDateTime.atZone(ZoneId.systemDefault()).toInstant())); // Date 타입으로 설정
            scheduleDTO.setWorkStartTime(workStartDateTime); // LocalDateTime으로 설정
            scheduleDTO.setWorkType("출근"); // work_type을 "출근"으로 설정

            // 출근 시간을 데이터베이스에 저장
            scheduleService.insertWorkStartTime(scheduleDTO);

            // 성공적으로 처리되었음을 응답
            return ResponseEntity.ok("출근 처리 완료: " + date + " " + startTime + " (사번: " + userCode + ")");
        } else {
            return ResponseEntity.status(401).body("사용자 정보가 없습니다."); // 인증되지 않은 경우
        }
    }

    @PostMapping("checkout")
    public ResponseEntity<String> checkOut(@RequestBody CheckOutRequestDTO checkOutRequest) {
        // 요청에서 받은 날짜와 시간을 처리
        String date = checkOutRequest.getDate(); // 예: "2024-12-11"
        String endTime = checkOutRequest.getEndTime(); // 예: "09:00:00"

        // 날짜를 LocalDate로 변환
        LocalDate workDate;
        try {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            workDate = LocalDate.parse(date, dateFormatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("날짜 형식이 잘못되었습니다.");
        }

        // 시간을 LocalTime으로 변환
        LocalTime workEndTime;
        try {
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            workEndTime = LocalTime.parse(endTime, timeFormatter);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body("시간 형식이 잘못되었습니다.");
        }

        // LocalDate와 LocalTime을 결합하여 LocalDateTime으로 변환
        LocalDateTime workEndDateTime = LocalDateTime.of(workDate, workEndTime);

        // 총 근무 시간 계산
        int totalHours = checkOutRequest.getTotalHours();
        int totalMinutes = checkOutRequest.getTotalMinutes();
        int totalSeconds = checkOutRequest.getTotalSeconds();

        // 총 근무 시간을 분으로 변환 (초를 분으로 변환)
        int totalWorkedMinutes = totalHours * 60 + totalMinutes + totalSeconds / 60;

        // 8시간 기준으로 정상 퇴근, 연장 구분
        String status;
        if (totalWorkedMinutes >= 510) { // 8시간 30분 = 510분
            status = "연장"; // 8시간 30분 이상 근무 시 연장으로 설정
        } else if (totalWorkedMinutes >= 480) { // 8시간 = 480분
            status = "정상"; // 8시간 근무 시 정상으로 설정
        } else if (totalWorkedMinutes > 0) {
            status = "조퇴"; // 8시간 미만 근무 시 조퇴로 설정
        } else {
            status = "결근"; // 근무 시간이 0인 경우
        }

        // 지각 여부 판단
        LocalTime officialStartTime = LocalTime.of(9, 0); // 공식 출근 시간
        if (workEndTime.isAfter(officialStartTime)) {
            status = "지각"; // 9시 1분 이후 출근 시 지각으로 설정
        }

        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            ScheduleDTO scheduleDTO = new ScheduleDTO();
            scheduleDTO.setEmpCode(userCode);

            // workEndDateTime을 Date로 변환
            Date workEndDate = Date.from(workEndDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // workEndDate를 ScheduleDTO에 설정
            scheduleDTO.setWorkDate(workEndDate); // Date 타입으로 설정
            scheduleDTO.setWorkEndTime(workEndDateTime); // LocalDateTime으로 설정
            scheduleDTO.setWorkType(status);

            // 퇴근 시간을 데이터베이스에 저장
            scheduleService.insertWorkEndTime(scheduleDTO);

            // 성공적으로 처리되었음을 응답
            return ResponseEntity.ok(status + " 처리 완료: " + date + " " + endTime + " (사번: " + userCode + ")");
        } else {
            return ResponseEntity.status(401).body("사용자 정보가 없습니다."); // 인증되지 않은 경우
        }
    }

    @GetMapping("getSchedule")
    public ResponseEntity<List<ScheduleDTO>> getSchedule() {

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode();
            List<ScheduleDTO> schedules = scheduleService.getSchedulesByUserCode(userCode);

            return ResponseEntity.ok(schedules);
        } else {
            return ResponseEntity.status(401).body(null); // 인증되지 않은 경우
        }
    }

    @GetMapping("checkin")
    public ResponseEntity<CheckInResponseDTO> getCheckInTime() {
        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            // 오늘 날짜를 가져옴
            LocalDate today = LocalDate.now();

            // 출근 시간을 가져옴
            LocalDateTime response = scheduleService.getCheckInTime(userCode, today.atStartOfDay());
            LocalDateTime checkInTime = response; // DTO에서 출근 시간 가져오기

            // CheckInResponseDTO 생성
            CheckInResponseDTO responseTime = new CheckInResponseDTO();
            responseTime.setEmpCode(userCode); // 사용자 코드 설정

            if (checkInTime != null) {
                responseTime.setCheckInTime(checkInTime); // LocalDateTime을 직접 설정
                return ResponseEntity.ok(responseTime);
            } else {
                responseTime.setCheckInTime(null); // 출근 시간이 없는 경우
                return ResponseEntity.status(404).body(responseTime);
            }
        } else {
            return ResponseEntity.status(401).body(null); // 인증되지 않은 경우
        }
    }

    @GetMapping("checkout")
    public ResponseEntity<CheckOutResponseDTO> getCheckOutTime() {
        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            // 오늘 날짜를 가져옴
            LocalDate today = LocalDate.now();

            // 출근 시간을 가져옴
            LocalDateTime response = scheduleService.getCheckOutTime(userCode, today.atStartOfDay());
            LocalDateTime checkOutTime = response; // DTO에서 출근 시간 가져오기

            // CheckInResponseDTO 생성
            CheckOutResponseDTO responseTime = new CheckOutResponseDTO();
            responseTime.setEmpCode(userCode); // 사용자 코드 설정

            if (checkOutTime != null) {
                responseTime.setCheckOutTime(checkOutTime); // LocalDateTime을 직접 설정
                return ResponseEntity.ok(responseTime);
            } else {
                responseTime.setCheckOutTime(null); // 출근 시간이 없는 경우
                return ResponseEntity.status(404).body(responseTime);
            }
        } else {
            return ResponseEntity.status(401).body(null); // 인증되지 않은 경우
        }
    }

    @GetMapping("vacation")
    public ResponseEntity<VacationDTO> getVacation() {
        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            // 사용자 정보를 기반으로 연차와 휴가 정보를 가져오는 로직 추가
            VacationDTO vacationDTO = scheduleService.getVacation(userCode); // 연차 정보 가져오기

            // ResponseEntity로 반환
            return ResponseEntity.ok(vacationDTO);
        } else {
            // 사용자 정보가 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getVacationInfo")
    public ResponseEntity< List<VacPaymentDTO>> getVactionInfo(){
        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            // 사용자 정보를 기반으로 연차와 휴가 정보를 가져오는 로직 추가
            List<VacPaymentDTO> vacPaymentDTO = scheduleService.getVactionInfo(userCode); // 연차 정보 가져오기


            // ResponseEntity로 반환
            return ResponseEntity.ok(vacPaymentDTO);
        } else {
            // 사용자 정보가 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getCacInfo")
    public ResponseEntity< List<CacPaymentDTO>> getCacInfo(){
        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            // 사용자 정보를 기반으로 경조사 정보 가져오는 로직 추가
            List<CacPaymentDTO> cacPaymentDTO = scheduleService.getCactionInfo(userCode); // 연차 정보 가져오기

            // ResponseEntity로 반환
            return ResponseEntity.ok(cacPaymentDTO);
        } else {
            // 사용자 정보가 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("getRetirementInfo")
    public ResponseEntity< List<RetirementDTO>> getRetirementInfo(){
        // UserInfoService를 통해 현재 로그인한 사용자의 정보를 가져옴
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (userInfo != null) {
            String userCode = userInfo.getCode(); // 사번 가져오기

            // 사용자 정보를 기반으로 경조사 정보 가져오는 로직 추가
            List<RetirementDTO> retirementDTO = scheduleService.getRetirementInfo(userCode); // 연차 정보 가져오기

            // ResponseEntity로 반환
            return ResponseEntity.ok(retirementDTO);
        } else {
            // 사용자 정보가 없을 경우 404 Not Found 반환
            return ResponseEntity.notFound().build();
        }
    }
}
