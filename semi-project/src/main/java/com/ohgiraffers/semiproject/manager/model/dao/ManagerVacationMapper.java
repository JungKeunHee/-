package com.ohgiraffers.semiproject.manager.model.dao;

import com.ohgiraffers.semiproject.manager.model.dto.VacationListDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacationManagementDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ManagerVacationMapper {

    // 직원정보과 휴가 갯수 가져오기
    List<VacationListDTO> getVacationList();

    // 연차 생성 후 더해주기
    void createAnnual(VacationManagementDTO vacationManagementDTO);

    // 휴가 생성 후 더해주기
    void createVacation(VacationManagementDTO vacationManagementDTO);

    // 연차 차감하기
    void deductedAnnual(VacationManagementDTO vacationManagementDTO);

    // 휴가 차감하기
    void deductedVacation(VacationManagementDTO vacationManagementDTO);
}
