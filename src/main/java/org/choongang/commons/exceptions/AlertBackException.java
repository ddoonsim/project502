package org.choongang.commons.exceptions;

import org.springframework.http.HttpStatus;

/**
 * 알림 메세지 띄운 후 뒤로 가기
 */
public class AlertBackException extends AlertException {
    public AlertBackException(String message, HttpStatus status) {
        super(message, status);
    }
}
