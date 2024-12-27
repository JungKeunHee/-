package com.ohgiraffers.semiproject.manager.model.dao;

import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.OverTimeDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerApprovalMapper {

    List<CacPaymentDTO> getCacPaymentList(String code);

    List<OverTimeDTO> getOverTimeList(String code);

    List<RetirementDTO> getRetirement(String code);

    List<VacPaymentDTO> getVacPayment(String code);

    // 연차 휴가 결재건 불러오기
    VacPaymentDTO getVacRequest(String documentNo);

    // 연차 휴가 도장 이미지 저장
    void vacationProcess(VacPaymentDTO process);

    // 승인 or 반려 값 가져오기
    VacPaymentDTO getVacationDetails(String documentNo);

    // 날짜 가져오기
    VacPaymentDTO getDate(int documentNo);

    // 경조사 정보 가져오기
    CacPaymentDTO getCacRequest(String documentNo);

    // 승인 or 반려 값 가져오기
    CacPaymentDTO getCacDetails(String documentNo);

    // 경조사 도장 이미지 저장
    void cacProcess(CacPaymentDTO process);

    // 연장근무 신청서 정보 가져오기
    OverTimeDTO getOverTimeRequest(String documentNo);

    // 승인 or 반려 값 가져오기
    OverTimeDTO getOverTimeDetails(String documentNo);

    // 연장근무 도장 이미지 저장
    void overTimeProcess(OverTimeDTO process);

    // 퇴직원 정보 가져오기
    RetirementDTO getRetirementRequest(String documentNo);

    // 승인 or 반려 값 가져오기
    RetirementDTO getRetirementDetails(String documentNo);

    // 퇴직원 도장 이미지 저장
    void retirementProcess(RetirementDTO process);
}
