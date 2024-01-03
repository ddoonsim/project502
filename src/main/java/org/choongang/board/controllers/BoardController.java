package org.choongang.board.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.board.entities.BoardData;
import org.choongang.board.repositories.BoardDataRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {
    
    private final BoardDataRepository boardDataRepository ;
    
    @ResponseBody
    @GetMapping("/test")
    public void test() {
        // 새 게시물 생성 시에 작성자 정보를 DB에 추가
        /*BoardData data = new BoardData() ;
        data.setSubject("제목");
        data.setContent("내용");
        boardDataRepository.saveAndFlush(data) ;*/

        // 게시물 수정 시에 수정자 정보를 DB에 추가
        BoardData data = boardDataRepository.findById(1L).orElse(null) ;
        data.setSubject("(수정)제목");
        boardDataRepository.flush();

    }

    @ResponseBody
    @GetMapping("/test2")
    //@PreAuthorize("hasAuthority('ADMIN')")    // 로그인했을 때만 접근이 가능
    @Secured({"ADMIN", "MANAGER"})
    public void test2() {
        System.out.println("test2🎈");
    }
}
