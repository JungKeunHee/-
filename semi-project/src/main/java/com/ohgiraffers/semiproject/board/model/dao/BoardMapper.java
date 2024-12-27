package com.ohgiraffers.semiproject.board.model.dao;

import com.ohgiraffers.semiproject.board.model.dto.BoardDTO;
import com.ohgiraffers.semiproject.board.model.dto.BoardEmpDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;

import java.util.List;

@Mapper
public interface BoardMapper {
    List<BoardEmpDTO> selectAll(@Param("offset") int offset, @Param("size") int size);

    long countAll();

    List<BoardEmpDTO> search(String query,
                                 @Param("offset") int offset,
                                 @Param("size") int size);

    long countSearch(String query);

    void boardRegist(BoardDTO board);

    void delete(Integer boardCode);

    BoardDTO title(Integer boardCode);

    void update(BoardDTO boardDTO);

    void viewCount(Integer boardCode);

    List<BoardDTO> select();
}