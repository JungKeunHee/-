package com.ohgiraffers.semiproject.schedule.model.dao;

import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import com.ohgiraffers.semiproject.schedule.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ScheduleMapper {

    // 출근시간 저장
    void insertWorkStartTime(ScheduleDTO scheduleDTO);

    // 퇴근시간 저장
    void insertWorkEndTime(ScheduleDTO scheduleDTO);

    List<ScheduleDTO> getSchedulesByUserCode(String userCode);

    CheckInResponseDTO getCheckInTime(String userCode, LocalDateTime localDateTime);

    CheckOutResponseDTO getCheckOutTime(String userCode, LocalDateTime localDateTime);

    VacationDTO getVacation(String userCode);

    ScheduleDTO getAttendanceByWorkStartTime(String userCode, String date);

    ScheduleDTO getAttendanceByWorkEndTime(String userCode, String date);

    void modifyAttendanceRequest(ScheduleDTO attendanceModifyInfo);

    List<ScheduleDTO> getAllAttendanceRequests(String userCode);

    void deleteAttendanceRequest(ScheduleDTO deleteAttendance);

    // 연차 휴가 신청서 정보 가져오기
    List<VacPaymentDTO> getVactionInfo(String userCode);

    // 승인된 신청서 연차 or 휴가 차감 후 업데이트
    void updateVacation(String code, VacationDTO vacationDTO);

    // 경조사 신청서 정보 가져오기
    List<CacPaymentDTO> getCacInfo(String userCode);

    // 퇴직원 신청서 정보 가져오기
    List<RetirementDTO> getRetirementInfo(String userCode);
}
