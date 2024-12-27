package com.ohgiraffers.semiproject.manager.model.service;

import com.ohgiraffers.semiproject.manager.model.dao.ManagerVacationMapper;
import com.ohgiraffers.semiproject.manager.model.dto.VacationListDTO;
import com.ohgiraffers.semiproject.manager.model.dto.VacationManagementDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ManagerVacationService {

    private final ManagerVacationMapper managerVacationMapper;

    public ManagerVacationService(ManagerVacationMapper managerVacationMapper){
        this.managerVacationMapper = managerVacationMapper;
    }

    // 직원정보와 휴가 갯수 가져오기
    public List<VacationListDTO> getVacationList() {
       return managerVacationMapper.getVacationList();
    }

    // 연차 생성 후 더해주기
    @Transactional
    public void createAnnual(VacationManagementDTO vacationManagementDTO) {
        managerVacationMapper.createAnnual(vacationManagementDTO);
    }

    // 휴가 생성 후 더해주기
    public void createVacation(VacationManagementDTO vacationManagementDTO) {
        managerVacationMapper.createVacation(vacationManagementDTO);
    }

    // 연차 차감해주기
    public void deductedAnnual(VacationManagementDTO vacationManagementDTO) {
        managerVacationMapper.deductedAnnual(vacationManagementDTO);
    }

    // 휴가 차감하기
    public void deductedVacation(VacationManagementDTO vacationManagementDTO) {
        managerVacationMapper.deductedVacation(vacationManagementDTO);
    }
}
