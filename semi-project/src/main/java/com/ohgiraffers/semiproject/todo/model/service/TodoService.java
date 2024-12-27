package com.ohgiraffers.semiproject.todo.model.service;

import com.ohgiraffers.semiproject.todo.model.dao.ToDoMapper;
import com.ohgiraffers.semiproject.todo.model.dto.ToDoDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TodoService {

    private final ToDoMapper toDoMapper;

    public TodoService(ToDoMapper toDoMapper){
        this.toDoMapper = toDoMapper;
    }

    public List<ToDoDTO> getToDoList(String code) {
        return toDoMapper.getToDoList(code);
    }

    @Transactional
    public void modifyTodo(ToDoDTO modifyTodo) {
        toDoMapper.modifyTodo(modifyTodo);
    }

    @Transactional
    public void deleteTodo(ToDoDTO deleteTodo) {
        toDoMapper.deleteTodo(deleteTodo);
    }

    @Transactional
    public void insertTodo(ToDoDTO insertTodo) {
        toDoMapper.insertTodo(insertTodo);
    }
}
