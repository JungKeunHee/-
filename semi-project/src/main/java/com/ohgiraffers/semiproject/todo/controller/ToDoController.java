package com.ohgiraffers.semiproject.todo.controller;

import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import com.ohgiraffers.semiproject.todo.model.dto.ToDoDTO;
import com.ohgiraffers.semiproject.todo.model.service.TodoService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
public class ToDoController {

    private final TodoService todoService;
    private final UserInfoService userInfoService;

    public ToDoController(TodoService todoService,
                          UserInfoService userInfoService){
        this.todoService = todoService;
        this.userInfoService = userInfoService;
    }

    @GetMapping("/api/getTodoList")
    public ResponseEntity<List<ToDoDTO>> getToDoList(){

        UserInfoResponse info = userInfoService.getUserInfo();

        String code = info.getCode();

        List<ToDoDTO> list = todoService.getToDoList(code);

        return ResponseEntity.ok(list);
    }

    @PostMapping("/api/editTodo")
    public String modifyTodo(@RequestBody ToDoDTO toDoDTO){

        ToDoDTO modifyTodo = new ToDoDTO();

        modifyTodo.setToDoCode(toDoDTO.getToDoCode());
        modifyTodo.setToDoContents(toDoDTO.getToDoContents());

        todoService.modifyTodo(modifyTodo);

        return "sidemenu/manager/manager";
    }

    @PostMapping("/api/deleteTodo")
    public String deleteTodo(@RequestBody ToDoDTO toDoDTO){

        ToDoDTO deleteTodo = new ToDoDTO();

        deleteTodo.setToDoCode(toDoDTO.getToDoCode());
        deleteTodo.setToDoContents(toDoDTO.getToDoContents());

        todoService.deleteTodo(deleteTodo);

        return "sidemenu/manager/manager";
    }

    @PostMapping("/insertTodo")
    public String insertTodo(@ModelAttribute ToDoDTO toDoDTO) {
        UserInfoResponse info = userInfoService.getUserInfo();
        String code = info.getCode();

        ToDoDTO insertTodo = new ToDoDTO();

        // 사용자 코드 설정
        insertTodo.setEmpCode(code);

        // 글 쓴 날짜
        insertTodo.setWriteDate(toDoDTO.getWriteDate()); // 날짜 설정

        // 할 일 내용 설정
        insertTodo.setToDoContents(toDoDTO.getToDoContents());

        todoService.insertTodo(insertTodo);

        return "sidemenu/manager/manager";
    }
}

