package com.ohgiraffers.semiproject.todo.model.dao;

import com.ohgiraffers.semiproject.todo.model.dto.ToDoDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ToDoMapper {

    List<ToDoDTO> getToDoList(String code);

    void modifyTodo(ToDoDTO modifyTodo);

    void deleteTodo(ToDoDTO deleteTodo);

    void insertTodo(ToDoDTO insertTodo);
}
