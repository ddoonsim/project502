package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.file.service.FileInfoService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

@Component    // 스프링 관리 객체로 등록
@RequiredArgsConstructor
public class Utils {

    private final HttpServletRequest request ;
    private final HttpSession session ;
    private final FileInfoService fileInfoService ;

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

    /**
     * \n 또는 \r\n --> <br>태그로 변환하는 메서드
     */
    public String nl2br(String str) {
        str = str.replaceAll("\\n", "<br>")    // 줄바꿈 태그로 변경
                .replaceAll("\\r", "") ;       // 리눅스와 맥에는 \r이 없기 때문에 제거

        return str ;
    }

    /**
     * 썸네일 이미지 사이즈를 admin의 기본 설정에서 저장한 값으로 설정
     */
    public List<int[]> getThumbSize() {
        BasicConfig config = (BasicConfig) request.getAttribute("siteConfig") ;
        String thumbSize = config.getThumbSize();    // \r\n
        String[] thumbsSize = thumbSize.split("\\n") ;
        List<int[]> data = Arrays.stream(thumbsSize)
                .filter(StringUtils::hasText)    // 빈 행 제거
                .map(s -> s.replaceAll("\\s+", ""))    // 문자열 내의 공백 모두 제거
                .map(this::toConvert).toList() ;

        return data ;
    }

    /**
     * nXn 형태의 썸네일 사이즈를 정수 배열로 변환
     */
    private int[] toConvert(String size) {
        size = size.trim() ;    // 앞, 뒤 공백 제거
        // \\r 제거, X로 두 수로 구분
        int[] data = Arrays.stream(size.replaceAll("\\r", "").toUpperCase().split("X"))
                .mapToInt(Integer::parseInt).toArray() ;

        return data ;
    }

    /**
     * view(타임리프)에서 이미지 태그로 썸네일 이미지 화면에 출력하는 편의 기능
     */
    public String printThumb(long seq, int width, int height, String className) {
        String[] data = fileInfoService.getThumb(seq, width, height);
        if (data != null) {
            String cls = StringUtils.hasText(className) ? " class='" + className + "'" : "" ;
            String image = String.format("<img src='%s'%s>", data[1], cls) ;
            return image ;
        }

        return "" ;
    }

    // 클래스 이름이 필요 없는 경우
    public String printThumb(long seq, int width, int height) {
        return printThumb(seq, width, height, null) ;
    }
}
