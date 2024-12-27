package com.ohgiraffers.semiproject.adoption.model.dao;

import com.ohgiraffers.semiproject.adoption.model.dto.AdoptionDTO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AdoptionMapper {

/* comment.------------------------ 입양진행중 Tab ------------------------ */
    // 진행중 조회
    List<AdoptionDTO> adoptingList();
    // 완료 조회
    List<AdoptionDTO> adoptCompletedList();
    // 취소 조회
    List<AdoptionDTO> adoptCanceledList();
    // 진행중 탭에서 입양취소로 상태 업데이트
    void updateByCanceled(String adoptNo);

    // 진행중 상세페이지
    AdoptionDTO adoptingDetail(String adoptNo);
    // 진행중 상세페이지에서 입양완료로 상태 업데이트
    void updateByCompleted(String adoptNo);


/* comment.------------------------ 입양완료 Tab ------------------------ */
    // 완료 상세페이지
    AdoptionDTO completedAdopterDetail(String adoptNo);
    // 입양완료 상세페이지에서 파양으로 상태 업데이트
    void updateByGiveUp(String adoptNo);

/* comment.------------------------ 입양취소 Tab ------------------------ */
    // 취소 상세페이지
    AdoptionDTO canceledAdopterDetail(String adoptNo);

}
