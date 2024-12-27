package com.ohgiraffers.semiproject.board.controller;


import com.ohgiraffers.semiproject.board.model.dto.BoardDTO;
import com.ohgiraffers.semiproject.board.model.dto.BoardEmpDTO;
import com.ohgiraffers.semiproject.board.model.dto.PageDTO;
import com.ohgiraffers.semiproject.board.model.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;
import java.util.List;

@Controller
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    //  게시판 전체 조회 및 페이징처리
    @GetMapping("/sidemenu/board")
    public String board(Model model,
                        @RequestParam(defaultValue = "0") int page,
                        @RequestParam(defaultValue = "15") int size) {

        int offset = page * size;

        List<BoardEmpDTO> boardList = boardService.selectAll(offset, size);

        long totalProducts = boardService.getTotalProducts();

        int totalPages = (int) Math.ceil((double) totalProducts / size);

        PageDTO pageInfo = new PageDTO(page, size, totalPages);

        model.addAttribute("boardList", boardList);

        model.addAttribute("pageInfo", pageInfo);

        return "sidemenu/board/board";
    }
    //  검색기능
    @GetMapping("sidemenu/board/search")
    public String search(@RequestParam String query,
                         @RequestParam(defaultValue = "0") int page,
                         @RequestParam(defaultValue = "15") int size,
                         Model model) {

        int offset = page * size;

        List<BoardEmpDTO> boardSearch = boardService.search(query, offset, size);

        long totalProducts = boardService.getTotalProducts1(query);

        int totalPages = (int) Math.ceil((double) totalProducts / size);

        if (totalPages == 0) {
            totalPages = 1;
        }

        PageDTO pageInfo = new PageDTO(page, size, totalPages);

        model.addAttribute("boardSearch", boardSearch);

        model.addAttribute("pageInfo", pageInfo);

        model.addAttribute("query", query);

        model.addAttribute("isEmpty", boardSearch.isEmpty());

        return "sidemenu/board/search";
    }

    //  글쓰기로가기
    @GetMapping("/sidemenu/board/regist")
    public void boardRegist(){}
    //  글쓰기
    @PostMapping("/sidemenu/board/regist")
    public String regist(@ModelAttribute BoardDTO board){

        Date now = new Date();

        board.setBoardDate(now);

        boardService.boardRegist(board);

        return "redirect:/sidemenu/board";
    }
    //  게시판 상세조회
    @GetMapping("/sidemenu/board/{boardCode}")
    public String title(@PathVariable Integer boardCode, Model model) {

        BoardDTO board = boardService.title(boardCode);

        boardService.viewConut(boardCode);

        model.addAttribute("board", board);

        return "sidemenu/board/title";
    }
    //  게시글 삭제
    @GetMapping("/sidemenu/board/{boardCode}/delete")
    public String delete(@PathVariable Integer boardCode) {

        boardService.delete(boardCode);

        return "redirect:/sidemenu/board";
    }
    //  수정창으로가기
    @GetMapping("/sidemenu/board/{boardCode}/update")
    public String update(@PathVariable Integer boardCode, Model model){

        BoardDTO board = boardService.title(boardCode);

        model.addAttribute("board", board);

        return "sidemenu/board/update";
    }
    //  게시판 수정
    @PostMapping("/sidemenu/board/update")
    public String boardUpdate(@ModelAttribute BoardDTO boardDTO) {

        boardService.update(boardDTO);

        return "redirect:/sidemenu/board";
    }
    //  메인으로 가기
    @GetMapping("/sidemenu/board/board")
    public void inventory() {}
}

