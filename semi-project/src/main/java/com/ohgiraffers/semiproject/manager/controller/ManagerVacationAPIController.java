package com.ohgiraffers.semiproject.manager.controller;

import com.ohgiraffers.semiproject.manager.model.dto.VacationListDTO;
import com.ohgiraffers.semiproject.manager.model.service.ManagerVacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ManagerVacationAPIController {

    private final ManagerVacationService managerVacationService;

    @Autowired
    public ManagerVacationAPIController(ManagerVacationService managerVacationService){
        this.managerVacationService = managerVacationService;
    }

    @GetMapping("/empVacationManagement")
    public ResponseEntity<List<VacationListDTO>> getVacationList() {

        List<VacationListDTO> list = managerVacationService.getVacationList();

        // 리스트가 비어있는 경우 204 No Content 응답
        if (list.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        // 리스트가 있는 경우 200 OK 응답과 함께 리스트 반환
        return ResponseEntity.ok(list);
    }
}
