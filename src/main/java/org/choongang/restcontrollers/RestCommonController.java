package org.choongang.restcontrollers;

import org.choongang.commons.exceptions.CommonException;
import org.choongang.commons.rests.JSONData;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * JSON 형식의 응답 컨트롤러
 */
@RestControllerAdvice("org.choongang.restcontrollers")
public class RestCommonController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<JSONData<Object>> errorHandler(Exception e) {

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR ;    // 500 에러를 default로
        if (e instanceof CommonException) {
            CommonException commonException = (CommonException) e ;
            status = commonException.getStatus() ;
        }

        JSONData<Object> data = new JSONData<>() ;
        data.setSuccess(false);
        data.setStatus(status);
        data.setMessage(e.getMessage());

        e.printStackTrace();

        return ResponseEntity.status(status).body(data) ;
    }
}
