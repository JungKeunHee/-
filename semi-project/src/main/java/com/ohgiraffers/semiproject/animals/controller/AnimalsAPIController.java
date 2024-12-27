package com.ohgiraffers.semiproject.animals.controller;

import com.ohgiraffers.semiproject.animals.model.dto.AnimalDTO;
import com.ohgiraffers.semiproject.animals.model.service.AnimalsService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AnimalsAPIController {

    private final AnimalsService animalsService;

    public AnimalsAPIController(AnimalsService animalsService){
        this.animalsService = animalsService;
    }

    @GetMapping("/api/breed-counts")
    public ResponseEntity<List<AnimalDTO>> getBreed(){

        List<AnimalDTO> list = animalsService.getBreed();

        return ResponseEntity.ok(list);
    }
}
