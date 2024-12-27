package com.ohgiraffers.semiproject.approval.model.dao;

import com.ohgiraffers.semiproject.manager.model.dto.CacPaymentDTO;
import com.ohgiraffers.semiproject.manager.model.dto.OverTimeDTO;
import com.ohgiraffers.semiproject.manager.model.dto.RetirementDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacPaymentDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ApprovalMapper {
    void insertVacForm(VacPaymentDTO vacPaymentDTO);

    void insertCacForm(CacPaymentDTO cacPaymentDTO);

    void insertOverTimeForm(OverTimeDTO overTimeDTO);

    void insertRetireMentForm(RetirementDTO retirementDTO);

    List<CacPaymentDTO> getMyCacPaymentList(String code);

    List<OverTimeDTO> getMyOverTimeList(String code);

    List<RetirementDTO> getMyRetirement(String code);

    List<VacPaymentDTO> getMyVacPayment(String code);

    // 경조사 데이터 삭제하기
    void deleteCacProcess(int no);

    // 연장근무 데이터 삭제하기
    void deleteOverTimeProcess(int no);

    // 퇴직서 데이터 삭제하기
    void deleteRetirementProcess(int no);

    // 연차 휴가 데이터 삭제하기
    void deleteVacationProcess(int no);
}
