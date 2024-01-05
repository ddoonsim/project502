package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.choongang.commons.exceptions.AlertBackException;
import org.choongang.commons.exceptions.AlertException;
import org.choongang.commons.exceptions.CommonException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;

public interface ExceptionProcessor {

    @ExceptionHandler(Exception.class)
    default String errorHandler(Exception e, Model model, HttpServletResponse response, HttpServletRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR ;

        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e ;
            status = commonException.getStatus() ;
        }

        response.setStatus(status.value());

        e.printStackTrace();

        if (e instanceof AlertException) {    // AlertException에 관한 예외 ==> 자바스크립트로 예외 view 처리
            String script = String.format("alert('%s');", e.getMessage()) ;    // 자바스크립트로 경고창 띄우기

            if (e instanceof AlertBackException) {    // AlertBackException에 속한 예외 ==> history.back(); 실행
                script += "history.back();" ;
            }

            model.addAttribute("script", script) ;
            return "common/_execute_script" ;
        }

        model.addAttribute("status", status.value()) ;  // 상태 코드
        model.addAttribute("path", request.getRequestURI()) ;  // 현재 경로
        model.addAttribute("method", request.getMethod()) ;  // 요청 방식
        model.addAttribute("message", e.getMessage()) ;  // 메세지

        return "error/common" ;
    }
}
