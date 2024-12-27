package com.ohgiraffers.semiproject.animals.model.service;

import com.ohgiraffers.semiproject.animals.model.dao.AnimalsMapper;
import com.ohgiraffers.semiproject.animals.model.dto.*;
import com.ohgiraffers.semiproject.employee.model.dto.CommentDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class AnimalsService {

    public final AnimalsMapper animalsMapper;

    @Autowired
    public AnimalsService(AnimalsMapper animalsMapper){
        this.animalsMapper = animalsMapper;
    }

// -----------------------------------------구조동물 페이지-----------------------------------------
    // 구조동물 전체조회 및 검색 동물조회
    public List<AnimalDTO> allAnimalAndSearchAnimals(int page, int limit, String animalCode, String typeCode, String breedCode, String gender, Date rescueDate) {

        int offset = (page - 1) * limit;

        return animalsMapper.AllAnimalAndSearchAnimals(offset, limit, animalCode, typeCode, breedCode, gender, rescueDate);
    }

    // 구조동물 전체조회 및 검색 동물조회 - 동물카운트
    public int getTotalAnimalCount(String animalCode, String typeCode, String breedCode, String gender, Date rescueDate) {
        return animalsMapper.getTotalAnimalCount(animalCode, typeCode, breedCode, gender, rescueDate);
    }

    // 구조동물 상세페이지
    public AnimalDTO detailAnimal(String animalCode) {return animalsMapper.detailAnimal(animalCode);}
    public AnimalDTO healthAnimal(String animalCode) {return animalsMapper.healthAnimal(animalCode);}
    public AnimalDTO inoculationAnimal(String animalCode) {return animalsMapper.inoculationAnimal(animalCode);}

    // 동물등록 - 품종 비동기처리
    public List<BreedDTO> findBreed() {
        return animalsMapper.findBreed();
    }
    // 동물등록 - 동물등록번호 자동입력
    public String autoAnimalCode() {
        String lastAnimalCode = animalsMapper.findLastAnimalCode();

        if (lastAnimalCode != null) {
            int lastNumber = Integer.parseInt(lastAnimalCode.substring(1));  // 'A' 제외한 숫자만 추출
            int nextNumber = lastNumber + 1;  // 1을 더함
            return "A" + String.format("%03d", nextNumber);  // 'A'와 3자리 숫자로 반환
        } else {
            return "A0010";  // 첫 번째 동물 코드
        }
    }
    // 동물등록
    @Transactional
    public void newAnimal(AnimalDTO animalDTO) {
        // 동물 등록
        animalsMapper.newAnimal(animalDTO);

        // animalCode가 등록 과정에서 생성되었다면 이를 animalDTO에 설정
        if (animalDTO.getAnimalCode() == null) {
            throw new IllegalStateException("animalCode is not set after newAnimal insertion.");
        }

        // 건강검진 삽입
        if (animalDTO.getHealthChecks() != null && !animalDTO.getHealthChecks().isEmpty()) {
            animalsMapper.insertHealthChecks(animalDTO.getAnimalCode(), animalDTO.getHealthChecks());
        }

        // 접종 삽입
        if (animalDTO.getInoculations() != null && !animalDTO.getInoculations().isEmpty()) {
            animalsMapper.insertInoculations(animalDTO.getAnimalCode(), animalDTO.getInoculations());
        }
    }

    // 삭제(체크박스 선택)
    @Transactional
    public void deleteBoard(String id) {
        animalsMapper.deleteBoard(id);
    }

// -----------------------------------------입양완료 페이지-----------------------------------------
    // 입양완료 페이지
    public List<AnimalDTO> adoptAnimalList(int page, int limit) {
        int offset = (page - 1) * limit; // 페이지 시작점 계산
        Map<String, Integer> params = new HashMap<>();
        params.put("limit", limit);
        params.put("offset", offset);
        return animalsMapper.adoptAnimalList(params);
    }
    // 입양완료 페이징처리
    public int getTotalAdoptAnimalCount() {
        return animalsMapper.getTotalAdoptAnimalCount();
    }

    // 입양완료동물 상세페이지
    public AnimalDTO adoptionDetailAnimal(String animalCode) {return animalsMapper.adoptionDetailAnimal(animalCode);}
    public AnimalDTO adoptionHealthAnimal(String animalCode) {return animalsMapper.adoptionHealthAnimal(animalCode);}
    public AnimalDTO adoptionInoculationAnimal(String animalCode) {return animalsMapper.adoptionInoculationAnimal(animalCode);}

// -----------------------------------------재고관리 페이지-----------------------------------------
    // 재고관리 조회
    public List<InventoryDTO> stock() {
        return animalsMapper.stock();
    }

    // 재고수량 수정
    @Transactional
    public void inventoryUpdate(int itemNum, String itemCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("itemNum", itemNum);
        params.put("itemCode", itemCode);

        animalsMapper.inventoryUpdate(params);
    }

// -----------------------------------------마이페이지(게시글내역)-----------------------------------------
    // 마이페이지 - 내 댓글 조회 메소드
    public List<AnimalDTO> getUserPosts(String code) {
        return animalsMapper.getUserPosts(code);
    }

    //품종 수 차트js 에 적용하기 위해 데이터 가져오기
    public List<AnimalDTO> getBreed() {
        return animalsMapper.getBreed();
    }
}
