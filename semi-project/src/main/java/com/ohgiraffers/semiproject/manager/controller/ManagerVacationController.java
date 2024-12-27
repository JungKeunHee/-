package com.ohgiraffers.semiproject.manager.controller;

import com.ohgiraffers.semiproject.manager.model.dto.VacationManagementDTO;
import com.ohgiraffers.semiproject.manager.model.service.ManagerVacationService;
import com.ohgiraffers.semiproject.schedule.model.dto.VacationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ManagerVacationController {

    private final ManagerVacationService managerVacationService;

    @Autowired
    public ManagerVacationController(ManagerVacationService managerVacationService){
        this.managerVacationService = managerVacationService;
    }

    // 연차 휴가 생성 후 더해주는 로직
    @PostMapping("/createVacation")
    public String createVacation(@RequestBody VacationManagementDTO vacationManagementDTO) {

        // leaveType에 따라 다른 메서드 호출
        if ("annual".equals(vacationManagementDTO.getLeaveType())) {

            managerVacationService.createAnnual(vacationManagementDTO);

        } else if ("vacation".equals(vacationManagementDTO.getLeaveType())) {

            managerVacationService.createVacation(vacationManagementDTO);
        }

        return "sidemenu/manager/vacationManagement";
    }

    // 연차 휴가 차감 하는 로직
    @PostMapping("/deductedVacation")
    public String deductedVacation(@RequestBody VacationManagementDTO vacationManagementDTO) {

        // leaveType에 따라 다른 메서드 호출
        if ("annual".equals(vacationManagementDTO.getLeaveType())) {

            managerVacationService.deductedAnnual(vacationManagementDTO);

        } else if ("vacation".equals(vacationManagementDTO.getLeaveType())) {

            managerVacationService.deductedVacation(vacationManagementDTO);
        }

        return "sidemenu/manager/vacationManagement";
    }
}
