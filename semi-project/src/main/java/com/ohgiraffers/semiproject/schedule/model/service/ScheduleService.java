package com.ohgiraffers.semiproject.schedule.model.service;

import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import com.ohgiraffers.semiproject.schedule.model.dao.ScheduleMapper;
import com.ohgiraffers.semiproject.schedule.model.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduleService {

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Transactional
    public void insertWorkStartTime(ScheduleDTO scheduleDTO) {

        scheduleMapper.insertWorkStartTime(scheduleDTO);
    }

    @Transactional
    public void insertWorkEndTime(ScheduleDTO scheduleDTO) {

        scheduleMapper.insertWorkEndTime(scheduleDTO);
    }

    public List<ScheduleDTO> getSchedulesByUserCode(String userCode) {

        return scheduleMapper.getSchedulesByUserCode(userCode);
    }

    public LocalDateTime getCheckInTime(String userCode, LocalDateTime localDateTime) {

        CheckInResponseDTO checkInResponseDTO = scheduleMapper.getCheckInTime(userCode, localDateTime);

        return checkInResponseDTO.getCheckInTime();
    }

    public LocalDateTime getCheckOutTime(String userCode, LocalDateTime localDateTime) {

        CheckOutResponseDTO checkOutResponseDTO = scheduleMapper.getCheckOutTime(userCode, localDateTime);

        return checkOutResponseDTO.getCheckOutTime();
    }

    public VacationDTO getVacation(String userCode) {

        VacationDTO vacationDTO =  scheduleMapper.getVacation(userCode);

        return vacationDTO;
    }

    public ScheduleDTO getAttendanceByWorkStartTime(String userCode, String date) {

        return scheduleMapper.getAttendanceByWorkStartTime(userCode, date);
    }

    public ScheduleDTO getAttendanceByWorkEndTime(String userCode, String date) {

        return scheduleMapper.getAttendanceByWorkEndTime(userCode, date);
    }

    @Transactional
    public void modifyAttendanceRequest(ScheduleDTO attendanceModifyInfo) {
        scheduleMapper.modifyAttendanceRequest(attendanceModifyInfo);
    }

    public List<ScheduleDTO> getAllAttendanceRequests(String userCode) {
        return scheduleMapper.getAllAttendanceRequests(userCode);
    }

    @Transactional
    public void deleteAttendanceRequest(ScheduleDTO deleteAttendance) {
        scheduleMapper.deleteAttendanceRequest(deleteAttendance);
    }

    public List<VacPaymentDTO> getVactionInfo(String userCode) {
        return scheduleMapper.getVactionInfo(userCode);
    }

    @Transactional
    public void updateVacation(String code, VacationDTO vacationDTO) {
        scheduleMapper.updateVacation(code, vacationDTO);
    }

    public List<CacPaymentDTO> getCactionInfo(String userCode) {
        return scheduleMapper.getCacInfo(userCode);
    }

    public List<RetirementDTO> getRetirementInfo(String userCode) {
        return scheduleMapper.getRetirementInfo(userCode);
    }
}
