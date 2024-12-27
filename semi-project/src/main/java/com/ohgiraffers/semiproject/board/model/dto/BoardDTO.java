package com.ohgiraffers.semiproject.board.model.dto;

import com.ohgiraffers.semiproject.employee.model.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_board")
public class BoardDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer boardCode;
    private String announceOk;
    private Date boardDate;
    private int viewCnt;
    private String boardTitle;
    private String boardContents;
    private String empCode;
}

