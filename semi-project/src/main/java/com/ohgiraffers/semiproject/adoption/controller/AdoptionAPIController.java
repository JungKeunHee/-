package com.ohgiraffers.semiproject.adoption.controller;

import com.ohgiraffers.semiproject.adoption.model.dto.AdoptionDTO;
import com.ohgiraffers.semiproject.adoption.model.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class AdoptionAPIController {

    private final AdoptionService adoptionService;

    @Autowired
    public AdoptionAPIController(AdoptionService adoptionService){
        this.adoptionService = adoptionService;
    }

    @GetMapping("/api/adoption-status")
    public ResponseEntity<List<AdoptionDTO>> getAdoption(){

        List<AdoptionDTO> list = adoptionService.adoptingList();

        return ResponseEntity.ok(list);
    }
}
