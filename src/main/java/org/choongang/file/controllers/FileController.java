package org.choongang.file.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.commons.ExceptionProcessor;
import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.CommonException;
import org.choongang.file.service.FileDeleteService;
import org.choongang.file.service.FileDownloadService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController implements ExceptionProcessor {

    private final FileDeleteService deleteService ;
    private final FileDownloadService downloadService ;

    @GetMapping("/delete/{seq}")
    public String delete(@PathVariable("seq") long seq, Model model) {
        deleteService.delete(seq);

        // 파일 삭제 후 후속 처리 --> 자바스크립트 함수로 처리
        String script = String.format("if (typeof parent.callbackFileDelete == 'function') parent.callbackFileDelete(%d);", seq) ;
        model.addAttribute("script", script) ;

        return "common/_execute_script";
    }

    /* 파일 다운로드의 원리 예시
    @ResponseBody
    @RequestMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq, HttpServletResponse response) throws IOException {
        // 화면(뷰페이지)에 출력될 Body데이터를 지정된 파일(test.txt)로 출력되게 설정
        response.setHeader("Content-Disposition", "attachment; filename=test.txt");

        // 다운로드된 파일 내부에 입력된 내용
        PrintWriter out = response.getWriter() ;
        out.println("test1");
        out.println("test2");
    }*/

    @ResponseBody
    @RequestMapping("/download/{seq}")
    public void download(@PathVariable("seq") Long seq) {
        try {
            downloadService.download(seq);
        }
        catch (CommonException e) {
            throw new AlertBackException(e.getMessage(), HttpStatus.NOT_FOUND) ;
        }
    }
}
