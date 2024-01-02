package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ResourceBundle;

@Component    // 스프링 관리 객체로 등록
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request ;
    private final HttpSession session ;

    // 메세지.properties
    private static final ResourceBundle commonsBundle ;
    private static final ResourceBundle validationsBundle ;
    private static final ResourceBundle errorsBundle ;
    // 클래스가 로드될 때 바로 실행될 코드
    static {
        // 필드 3개 한번에 초기화
        commonsBundle = ResourceBundle.getBundle("messages.commons") ;
        validationsBundle = ResourceBundle.getBundle("messages.validations") ;
        errorsBundle = ResourceBundle.getBundle("messages.errors") ;
    }

    /**
     * device 모드 수동 전환 혹은
     * 요청 헤더의 User-Agent 정보를 가지고 모바일인지 체크
     */
    public boolean isMobile() {
        // 모바일 수동 전환 모드 체크
        String device = (String) session.getAttribute("device") ;
        if (StringUtils.hasText(device)) {
            return device.equals("MOBILE") ;
        }

        String ua = request.getHeader("User-Agent") ;  // User-Agent 가져오기

        // 모바일 장비 체크를 위한 정규 표현식
        String pattern = ".*(iPhone|iPod|iPad|BlackBerry|Android|Windows CE|LG|MOT|SAMSUNG|SonyEricsson).*" ;

        return ua.matches(pattern) ;    // 모바일이면 true
    }

    /**
     * device가 모바일인지 PC인지에 따라 view 경로 분리
     */
    public String tpl(String path) {
        String prefix = isMobile() ? "mobile/" : "front/" ;

        return prefix + path ;
    }

    /**
     * resources.messages의 .properties의 메세지들을 키로 가져오는 메서드
     */
    public static String getMessage(String code, String type) {
        type = StringUtils.hasText(type) ? type : "validations" ;

        ResourceBundle bundle = null ;
        // 타입에 따라 번들 대입
        if (type.equals("commons")) {
            bundle = commonsBundle ;
        } else if (type.equals("errors")) {
            bundle = errorsBundle ;
        } else {
            bundle = validationsBundle ;
        }

        return bundle.getString(code);
    }

    public static String getMessage(String code) {
        return getMessage(code, null) ;
    }
}
