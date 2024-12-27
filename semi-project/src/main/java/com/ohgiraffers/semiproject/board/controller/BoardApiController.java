package com.ohgiraffers.semiproject.board.controller;

import com.ohgiraffers.semiproject.board.model.dto.BoardDTO;
import com.ohgiraffers.semiproject.board.model.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    @GetMapping("/api/board")
    @ResponseBody
    public ResponseEntity<List<BoardDTO>> getBoardData() {
        List<BoardDTO> boardDTOList = boardService.select();

        if (boardDTOList == null || boardDTOList.isEmpty()) {
            return ResponseEntity.notFound().build(); // 데이터가 없을 경우 404 반환
        }

        return ResponseEntity.ok(boardDTOList); // 정상적으로 BoardDTO 리스트 반환
    }
}
