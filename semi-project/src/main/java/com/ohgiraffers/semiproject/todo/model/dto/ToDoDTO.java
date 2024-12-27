package com.ohgiraffers.semiproject.todo.model.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class ToDoDTO {

    private int toDoCode;
    private String empCode;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date writeDate;
    private String toDoContents;
}
