package com.ohgiraffers.semiproject.board.model.service;

import com.ohgiraffers.semiproject.board.model.dao.BoardMapper;
import com.ohgiraffers.semiproject.board.model.dto.BoardDTO;
import com.ohgiraffers.semiproject.board.model.dto.BoardEmpDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final BoardMapper boardMapper;

    public BoardService(BoardMapper boardMapper) {
        this.boardMapper = boardMapper;
    }

    public List<BoardDTO> select() {
        return boardMapper.select();
    }

    public List<BoardEmpDTO> selectAll(int offset, int size) {

        List<BoardEmpDTO> result = boardMapper.selectAll(offset, size);

        return result;
    }

    public List<BoardEmpDTO> search(String query, int offset, int size) {

        List<BoardEmpDTO> result = boardMapper.search(query, offset, size);

        return result;
    }

    @Transactional
    public void boardRegist(BoardDTO board) {

        boardMapper.boardRegist(board);
    }

    @Transactional
    public void update(BoardDTO boardDTO) {

        boardMapper.update(boardDTO);
    }

    @Transactional
    public void delete(Integer boardCode) {

        boardMapper.delete(boardCode);
    }

    public BoardDTO title(Integer boardCode) {

        return boardMapper.title(boardCode);
    }

    public void viewConut(Integer boardCode) {

        boardMapper.viewCount(boardCode);
    }

    public long getTotalProducts() {
            return boardMapper.countAll();
    }

    public long getTotalProducts1(String query) {
            return boardMapper.countSearch(query);
    }
}
