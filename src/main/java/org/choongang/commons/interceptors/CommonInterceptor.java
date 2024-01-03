package org.choongang.commons.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * 공통 인터셉터
 */
@Component
public class CommonInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        checkDevice(request);    // 함수로 기능 분리
        clearLoginData(request) ;    // 로그인 경고 메세지가 저장되어 있는 세션 비우기

        return true ;
    }

    /**
     * device의 PC or mobile 수동 변경 처리
     */
    private void checkDevice(HttpServletRequest request) {
        // device : PC ==> PC 뷰 | mobile ==> mobile 뷰
        String device = request.getParameter("device") ;
        if (!StringUtils.hasText(device)) {    // device 파라미터 값이 없을 때
            return;
        }
        // device 파라미터 값이 있을 때
        device = device.toUpperCase().equals("MOBILE") ? "MOBILE" : "PC" ;

        HttpSession session = request.getSession();
        session.setAttribute("device", device);
    }

    /**
     * 다른 페이지에서 로그인 페이지로 재접속 시에는 오류 메세지 데이터 지우기
     */
    private void clearLoginData(HttpServletRequest request) {
        String URL = request.getRequestURI() ;    // 요청 URL
        if (URL.indexOf("/member/login") == -1) {
            // 요청 URL이 로그인페이지가 아니면 세션 비우기
            HttpSession session = request.getSession();
            MemberUtil.clearLoginData(session);
        }
    }
}
