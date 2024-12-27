package com.ohgiraffers.semiproject.employee.controller;

import com.ohgiraffers.semiproject.employee.model.dto.EmployeeScheduleInfoDTO;
import com.ohgiraffers.semiproject.employee.model.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class EmployeeAPIController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeAPIController(EmployeeService employeeService){
        this.employeeService = employeeService;
    }

    @GetMapping("/api/employee")
    public ResponseEntity<List<EmployeeScheduleInfoDTO>> getEmployeeList(){

        List<EmployeeScheduleInfoDTO> list = employeeService.getEmployeeList();

        return ResponseEntity.ok(list);
    }
}
