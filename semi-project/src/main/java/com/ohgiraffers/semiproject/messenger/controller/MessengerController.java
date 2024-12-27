package com.ohgiraffers.semiproject.messenger.controller;

import com.ohgiraffers.semiproject.messenger.model.dto.MessengerDTO;
import com.ohgiraffers.semiproject.messenger.model.service.MessengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class MessengerController {

    @Autowired
    private MessengerService service;

    @GetMapping("/sidemenu/messenger")
    public String showMessenger() {

        return "sidemenu/messenger/messenger";
    }

    @GetMapping("/employees")
    @ResponseBody
    public List<MessengerDTO> getAllEmployees() {
        List<MessengerDTO> test = service.findEmployees();
        return test;
    }

}
