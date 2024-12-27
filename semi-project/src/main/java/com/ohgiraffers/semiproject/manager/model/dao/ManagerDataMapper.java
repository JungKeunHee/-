package com.ohgiraffers.semiproject.manager.model.dao;

import com.ohgiraffers.semiproject.manager.model.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerDataMapper {

    // 근태수정 요청 대기중 카운트 수 가져오기
    List<ManagerScheduleDTO> getScheduleList(String code);

    // 경조사 대기중 카운트 수 가져오기
    List<CacPaymentDTO> getCacPaymentList(String code);

    // 연장 결재 대기중 카운트 수 가져오기
    List<OverTimeDTO> getOverTimeList(String code);

    // 퇴직원 결재 대기중 카운트 수 가져오기
    List<RetirementDTO> getRetirement(String code);

    // 연차 결재 대기중 카운트 수 가져오기
    List<VacPaymentDTO> getAnnualList(String code);

    // 휴가 결재 대기중 카운트 수 가져오기
    List<VacPaymentDTO> getVacationList(String code);
}
