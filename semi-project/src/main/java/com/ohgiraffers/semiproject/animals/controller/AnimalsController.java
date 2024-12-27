package com.ohgiraffers.semiproject.animals.controller;

import com.ohgiraffers.semiproject.animals.model.dto.*;
import com.ohgiraffers.semiproject.animals.model.service.AnimalsService;
import com.ohgiraffers.semiproject.main.model.dto.UserInfoResponse;
import com.ohgiraffers.semiproject.main.model.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AnimalsController {

    private final AnimalsService animalsService;
    private final UserInfoService userInfoService;

    @Autowired
    public AnimalsController(AnimalsService animalsService, UserInfoService userInfoService){
        this.animalsService = animalsService;
        this.userInfoService = userInfoService;
    }

// -----------------------------------------구조동물 페이지-----------------------------------------
    // 구조 동물 페이지
    @GetMapping("/sidemenu/animals")
    public String animals(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "8") int limit,
                          @RequestParam(required = false) String animalCode,
                          @RequestParam(required = false) String typeCode,
                          @RequestParam(required = false) String breedCode,
                          @RequestParam(required = false) String gender,
                          @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date rescueDate,
                          Model model) {

        List<AnimalDTO> list = animalsService.allAnimalAndSearchAnimals(page, limit, animalCode, typeCode, breedCode, gender, rescueDate);
        model.addAttribute("list", list);

        // 페이지 네비게이션 정보 추가
        int totalRecords = animalsService.getTotalAnimalCount(animalCode, typeCode, breedCode, gender, rescueDate); // 총 레코드 수 조회
        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        model.addAttribute("totalRecords",totalRecords);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);
        model.addAttribute("limit", limit);

        return "sidemenu/animals/animals";
    }

    @GetMapping("sidemenu/animals/animals")
    public void animalHome(){}

    // 구조동물 상세페이지
    @GetMapping("/animals/detailAnimal/{animalCode}")
    public String detailAnimal(@PathVariable String animalCode,Model model){

        AnimalDTO detail = animalsService.detailAnimal(animalCode);
        model.addAttribute("detail", detail);

        AnimalDTO health = animalsService.healthAnimal(animalCode);
        model.addAttribute("health", health);

        AnimalDTO inoculate = animalsService.inoculationAnimal(animalCode);
        model.addAttribute("inoculate", inoculate);

        return "sidemenu/animals/detailAnimal";
    }

    // 동물등록번호 등록 시 자동부여
    @GetMapping("/sidemenu/animals/insert")
    public String addAnimal(Model model){
        String animalCode = animalsService.autoAnimalCode();

        UserInfoResponse userInfo = userInfoService.getUserInfo();

        model.addAttribute("animalCode", animalCode);

        model.addAttribute("userInfo", userInfo);

        return "sidemenu/animals/insert";
    }
    @GetMapping(value = "/sidemenu/animals/breed", produces = "application/json; charset=UTF-8")
    @ResponseBody
    public List<BreedDTO> findCategoryList(){
        return animalsService.findBreed();
    }

    // 구조동물 등록
    @PostMapping("/sidemenu/animals/insert")
    public String newAnimal(@ModelAttribute AnimalDTO animalDTO,
                            @RequestParam List<String> healthChecks, // 수동으로 받기
                            @RequestParam List<String> inoculations){


        // 사용자 정보 가져오기
        UserInfoResponse userInfo = userInfoService.getUserInfo();
        String empCode = userInfo.getCode();

        // 담당자 설정
        EmpDTO empDTO = new EmpDTO();
        empDTO.setEmpCode(empCode);
        animalDTO.setEmpDTO(empDTO);

        animalDTO.setHealthChecks(healthChecks.stream()
                .map(code -> new HealthCheckDTO(code))
                .collect(Collectors.toList()));
        animalDTO.setInoculations(inoculations.stream()
                .map(code -> new InoculationDTO(code))
                .collect(Collectors.toList()));
        System.out.println("animalDTO = " + animalDTO);
        // 동물 등록 처리
        animalsService.newAnimal(animalDTO);

        return "redirect:/sidemenu/animals";
    }


    // 체크박스 선택 후 삭제
    @PostMapping("/delete")
    public String delete(@RequestParam List<String> boardIds){

        for(int i=0; i<boardIds.size(); i++){
            String id = boardIds.get(i);
            animalsService.deleteBoard(id);
            System.out.println("id = " + id);
        }

        return "redirect:/sidemenu/animals";
    }

// -----------------------------------------입양완료 페이지-----------------------------------------
    // 입양완료 페이지
    @GetMapping("/sidemenu/adoptionComplete")
    public String adoptionComplete(@RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "10") int limit,
                                   Model model){
        List<AnimalDTO> adoptList = animalsService.adoptAnimalList(page, limit);
        model.addAttribute("adoptList", adoptList);

        // 페이지 네비게이션 정보 추가
        int totalRecords = animalsService.getTotalAdoptAnimalCount(); // 총 레코드 수 조회
        int totalPages = (int) Math.ceil((double) totalRecords / limit);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", page);

        return "sidemenu/animals/adoptionComplete";
    }

    // 구조동물 상세페이지
    @GetMapping("/adoptionComplete/detailAnimal/{animalCode}")
    public String adoptionCompleteDetailAnimal(@PathVariable String animalCode,Model model){

        AnimalDTO detail = animalsService.adoptionDetailAnimal(animalCode);
        model.addAttribute("detail", detail);

        AnimalDTO health = animalsService.adoptionHealthAnimal(animalCode);
        model.addAttribute("health", health);

        AnimalDTO inoculate = animalsService.adoptionInoculationAnimal(animalCode);
        model.addAttribute("inoculate", inoculate);

        return "sidemenu/animals/adoptionDetailAnimal";
    }

// -----------------------------------------재고관리 페이지-----------------------------------------
    // 재고관리 페이지로 이동
    @GetMapping("/sidemenu/stock")
    public String stock(Model model){

        List<InventoryDTO> inventoryList = animalsService.stock();

        model.addAttribute("inventoryList", inventoryList);

        return "sidemenu/animals/stock";
    }

    @PostMapping("/inventory/update")
    public String inventoryUpdate(@RequestParam int itemNum,
                                  @RequestParam String itemCode,
                                  RedirectAttributes redirectAttributes) {

        animalsService.inventoryUpdate(itemNum, itemCode); // 재고 업데이트

        // 쿼리 파라미터로 updateSuccess 값을 전달
        redirectAttributes.addAttribute("updateSuccess", true);

        return "redirect:/sidemenu/stock";
    }


}
