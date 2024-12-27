package com.ohgiraffers.semiproject.board.model.dto;

import com.ohgiraffers.semiproject.employee.model.dto.EmployeeDTO;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class BoardEmpDTO {

    private Integer boardCode;
    private String announceOk;
    private Date boardDate;
    private int viewCnt;
    private String boardTitle;
    private String boardContents;
    private String empCode;
    private EmployeeDTO employeeDTO;
}
