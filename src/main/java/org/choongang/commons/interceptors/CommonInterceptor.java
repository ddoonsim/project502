package org.choongang.commons.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.member.MemberUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;

/**
 * 공통 인터셉터
 */
@Component
@RequiredArgsConstructor
public class CommonInterceptor implements HandlerInterceptor {

    private final ConfigInfoService infoService ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        checkDevice(request);    // 함수로 기능 분리
        clearLoginData(request) ;    // 로그인 경고 메세지가 저장되어 있는 세션 비우기
        loadSiteConfig(request) ;

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

    /**
     * admin의 기본설정을 페이지마다 적용하는 메서드
     *     ## controller 뿐만 아니라 모든 css, js파일 로드할 때마다 쿼리 실행하는 문제 발생
     */
    private void loadSiteConfig(HttpServletRequest request) {
        // 로드할 때 쿼리 실행에서 제외할 파일들의 확장자
        String[] excludes = {".js", ".css", ".png", ".jpg", ".jpeg", "gif", ".pdf", ".xls", ".xlxs", ".ppt"} ;
        String URL = request.getRequestURI().toLowerCase() ;
        boolean isIncluded = Arrays.stream(excludes).anyMatch(s -> URL.contains(s)) ;    // URL문자열 내에 excludes 중에서 일치하는 문자열이 있으면 true 반환
        if (isIncluded) {
            // true 이면 쿼리 실행 X하고 메서드 종료
            return;
        }

        BasicConfig config = infoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new) ;

        request.setAttribute("siteConfig", config);
    }
}
