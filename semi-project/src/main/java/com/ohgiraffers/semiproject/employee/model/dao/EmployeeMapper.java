package com.ohgiraffers.semiproject.employee.model.dao;

import com.ohgiraffers.semiproject.employee.model.dto.CommentDTO;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeDTOJOB;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeJoinListDTO;
import com.ohgiraffers.semiproject.employee.model.dto.EmployeeScheduleInfoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface EmployeeMapper {

    List<EmployeeDTOJOB> empAll(@Param("offset") int offset, @Param("size") int size);

    long countAll();

    List<EmployeeDTOJOB> empSearch(String query, @Param("offset") int offset,
                                                 @Param("size") int size);

    long countAll1(String query);

    EmployeeDTOJOB empSelect(String empCode);

    void saveComment(CommentDTO commentDTO);

    List<CommentDTO> comment(String empCode);

    List<EmployeeJoinListDTO> empAllSelect();

    void commentDelete(CommentDTO commentDTO);

    List<EmployeeJoinListDTO> mailAllSelect(String code);

    // 마이페이지 - 내 댓글 조회 메소드
    List<CommentDTO> getUserComment(String code);
    // 마이페이지 - 비밀번호 변경
    int updatePasswordByCode(String encryptedPassword, String code);
    // 마이페이지 - 회원정보 수정
    EmployeeJoinListDTO empInfoSelect(String empCode);

    // 직원별 사번코드, 부서명, 직급명, 당일 출근시간, 퇴근시간 조회해오기
    List<EmployeeScheduleInfoDTO> getEmployeeList();
    // 마이페이지 - 회원정보 이미지 수정
    int changeProfileImage(String fileName, String empCode);
    // 마이페이지 - 회원정보 이미지 삭제
    int deleteProfileImage(String empCode);
}
