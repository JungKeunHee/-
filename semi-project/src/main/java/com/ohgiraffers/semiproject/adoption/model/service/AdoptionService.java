package com.ohgiraffers.semiproject.adoption.model.service;

import com.ohgiraffers.semiproject.adoption.model.dao.AdoptionMapper;
import com.ohgiraffers.semiproject.adoption.model.dto.AdoptionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdoptionService {

    private final AdoptionMapper adoptionMapper;

    public AdoptionService(AdoptionMapper adoptionMapper){
        this.adoptionMapper = adoptionMapper;
    }

/* comment. --------------------------------- 입양진행중 Tab --------------------------------- */
    // 진행중 조회
    public List<AdoptionDTO> adoptingList() {return adoptionMapper.adoptingList();}
    // 완료 조회
    public List<AdoptionDTO> adoptCompletedList() {return  adoptionMapper.adoptCompletedList();}
    // 취소 조회
    public List<AdoptionDTO> adoptCanceledList() {return adoptionMapper.adoptCanceledList();}
    // 진행중 탭에서 입양취소로 상태 업데이트
    public void updateByCanceled(String adoptNo) {adoptionMapper.updateByCanceled(adoptNo);}
    // 진행중 상세페이지
    public AdoptionDTO adoptingDetail(String adoptNo) {return adoptionMapper.adoptingDetail(adoptNo);}
    @Transactional
    // 진행중 상세페이지에서 입양완료로 상태 업데이트
    public void updateByCompleted(String adoptNo) {adoptionMapper.updateByCompleted(adoptNo);}

/* comment.--------------------------------- 입양완료 Tab --------------------------------- */
    // 완료 상세페이지
    public AdoptionDTO completedAdopterDetail(String adoptNo) {return adoptionMapper.completedAdopterDetail(adoptNo);}

    @Transactional
    // 입양완료 상세페이지에서 파양으로 상태 업데이트
    public void updateByGiveUp(String adoptNo) {adoptionMapper.updateByGiveUp(adoptNo);}
/* comment.--------------------------------- 입양취소 Tab --------------------------------- */
    // 취소 상세페이지
    public AdoptionDTO canceledAdopterDetail(String adoptNo){return adoptionMapper.canceledAdopterDetail(adoptNo);}

}
