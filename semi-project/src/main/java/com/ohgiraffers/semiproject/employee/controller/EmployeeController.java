package com.ohgiraffers.semiproject.employee.controller;

import com.ohgiraffers.semiproject.board.model.dto.PageDTO;
import com.ohgiraffers.semiproject.employee.model.dto.CommentDTO;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeDTOJOB;
import com.ohgiraffers.semiproject.employee.model.service.EmployeeService;
import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {

    private final EmployeeService employeeService;
    private final UserInfoService userInfoService;

    public EmployeeController(EmployeeService employeeService,UserInfoService userInfoService) {
        this.employeeService = employeeService;
        this.userInfoService = userInfoService;
    }
    // 직원 전체 조회
    @GetMapping("sidemenu/employee")
    public String empAll(Model model, @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "15") int size) {

        int offset = page * size;

        List<EmployeeDTOJOB> emp = employeeService.empAll(offset, size);

        long totalProducts = employeeService.getTotalProducts();

        int totalPages = (int) Math.ceil((double) totalProducts / size);

        PageDTO pageInfo = new PageDTO(page, size, totalPages);

        model.addAttribute("pageInfo", pageInfo);

        model.addAttribute("emp", emp);

        return "sidemenu/employee/employee";
    }

    @GetMapping("/sidemenu/employee/search")
    public String empSearch(@RequestParam String query, Model model,
                            @RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "15") int size) {

        int offset = page * size;

        List<EmployeeDTOJOB> emp = employeeService.empSearch(query, offset, size);

        long totalProducts = employeeService.getTotalProducts1(query);

        int totalPages = (int) Math.ceil((double) totalProducts / size);

        if (totalPages == 0) {
            totalPages = 1;
        }

        PageDTO pageInfo = new PageDTO(page, size, totalPages);

        model.addAttribute("pageInfo", pageInfo);

        model.addAttribute("emp", emp);

        model.addAttribute("query", query);

        model.addAttribute("isEmpty", emp.isEmpty());

        return "sidemenu/employee/search";
    }

    @GetMapping("/employee/details/{empCode}")
    public String getEmployeeDetails(@PathVariable String empCode, Model model) {


        EmployeeDTOJOB employee = employeeService.empSelect(empCode);

        List<CommentDTO> comment = employeeService.comment(empCode);

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        if (comment == null) {
            comment = new ArrayList<>();
        }

        String name = (userInfo != null) ? userInfo.getName() : "익명 사용자";

        model.addAttribute("name", name);

        model.addAttribute("comment", comment);

        model.addAttribute("emp", employee);

        return "sidemenu/employee/empdetail";
    }

    @PostMapping("/comments/add")
    public String addComment(@RequestParam String empCode, @RequestParam String text) {
        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setEmpCode(empCode);

        commentDTO.setText(text);

        commentDTO.setCommentEmpCode(code);

        LocalDateTime createdDate = LocalDateTime.now();

        commentDTO.setCreatedDate(createdDate);

        employeeService.saveComment(commentDTO);

        return "redirect:/employee/details/" + empCode;
    }

    @GetMapping("/sidemenu/employee/{empCode}/comment/{id}/delete")
    public String commentDelete(@PathVariable String empCode, @PathVariable Integer id) {

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        String code = userInfo.getCode();

        CommentDTO commentDTO = new CommentDTO();

        commentDTO.setEmpCode(empCode);

        commentDTO.setId(id);

        commentDTO.setCommentEmpCode(code);

        employeeService.commentDelete(commentDTO);

        return "redirect:/employee/details/" + empCode;
    }

    @GetMapping("/sidemenu/employee/employee")
    public void employee() {
    }
}

