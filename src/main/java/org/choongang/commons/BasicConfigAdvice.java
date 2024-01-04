package org.choongang.commons;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.service.ConfigInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 사이트 설정을 프로젝트 전체에 적용하는 컨트롤러
 */
@ControllerAdvice("org.choongang")
@RequiredArgsConstructor
public class BasicConfigAdvice {

    private final ConfigInfoService infoService ;

    @ModelAttribute("siteConfig")
    public Map<String, String> getBasicConfig() {
        Optional<Map<String, String>> opt = infoService.get("basic", new TypeReference<>() {});

        Map<String, String> config = opt.orElseGet(HashMap::new) ;

        return config ;
    }
}