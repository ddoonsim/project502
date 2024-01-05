package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 자바 스크립트로 예외 알림
 */
public class AlertException extends CommonException{
    public AlertException(String message, HttpStatus status) {
        super(message, status);
    }
}
