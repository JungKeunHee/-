package com.ohgiraffers.semiproject.adoption.controller;

import com.ohgiraffers.semiproject.adoption.model.dto.AdoptionDTO;
import com.ohgiraffers.semiproject.adoption.model.service.AdoptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdoptionController {

    private final AdoptionService adoptionService;

    @Autowired
    public AdoptionController(AdoptionService adoptionService){
        this.adoptionService = adoptionService;
    }

/* comment.--------------------------------- 입양진행중 Tab --------------------------------- */
    // 입양 페이지로 이동
    @GetMapping("/sidemenu/adoption")
    public String adoptionList(Model model){

        List<AdoptionDTO> adoptingList = adoptionService.adoptingList();
        model.addAttribute("adoptingList", adoptingList);

        List<AdoptionDTO> adoptCompletedList = adoptionService.adoptCompletedList();
        model.addAttribute("adoptCompletedList", adoptCompletedList);

        List<AdoptionDTO> adoptCanceledList = adoptionService.adoptCanceledList();
        model.addAttribute("adoptCanceledList", adoptCanceledList);

        return "sidemenu/adoption/adoption";
    }

    // 입양진행중 탭에서 입양취소로 상태 업데이트
    @GetMapping("/adopterCanceled/{adoptNo}")
    public String updateByCanceled(@PathVariable String adoptNo){

        adoptionService.updateByCanceled(adoptNo);

        return "redirect:/sidemenu/adoption?tab=canceled";
    }

    // 입양진행중 상세페이지
    @GetMapping("/adopterDetail/adopting/{adoptNo}")
    public String adoptingAdopterDetail(@PathVariable String adoptNo, Model model){

        AdoptionDTO adoptingDetail = adoptionService.adoptingDetail(adoptNo);
        model.addAttribute("adoptingDetail", adoptingDetail);

        return "sidemenu/adoption/adoptingDetail";
    }

    // 입양진행중 상세페이지에서 입양완료로 상태 업데이트
    @GetMapping("/adoptCompleted/{adoptNo}")
    public String updateByCompleted(@PathVariable String adoptNo){

        adoptionService.updateByCompleted(adoptNo);

        return "redirect:/sidemenu/adoption?tab=completed";
    }

/* comment.--------------------------------- 입양완료 Tab --------------------------------- */
    // 입양완료 상세페이지
    @GetMapping("/adopterDetail/completed/{adoptNo}")
    public String completedAdopterDetail(@PathVariable String adoptNo, Model model){

        AdoptionDTO completedDetail = adoptionService.completedAdopterDetail(adoptNo);
        model.addAttribute("completedDetail", completedDetail);

        return "sidemenu/adoption/adoptCompletedDetail";
    }

    // 입양완료 상세페이지에서 파양으로 상태 업데이트
    @GetMapping("/giveUp/{adoptNo}")
    public String updateByGiveUp(@PathVariable String adoptNo){

        adoptionService.updateByGiveUp(adoptNo);

        return "redirect:/sidemenu/adoption?tab=canceled";
    }

/* comment.--------------------------------- 입양취소 Tab --------------------------------- */
    // 입양취소 상세페이지
    @GetMapping("/adopterDetail/canceled/{adoptNo}")
    public String canceledAdopterDetail(@PathVariable String adoptNo, Model model){

        AdoptionDTO canceledDetail = adoptionService.canceledAdopterDetail(adoptNo);
        model.addAttribute("canceledDetail", canceledDetail);

        return "sidemenu/adoption/adoptCanceledDetail";
    }
}
