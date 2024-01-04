package org.choongang.commons;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.controllers.BasicConfig;
import org.choongang.admin.config.service.ConfigInfoService;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * 사이트 설정을 프로젝트 전체에 적용하는 컨트롤러
 */
@ControllerAdvice("org.choongang")
@RequiredArgsConstructor
public class BasicConfigAdvice {

    private final ConfigInfoService infoService ;

    @ModelAttribute("siteConfig")
    public BasicConfig getBasicConfig() {
        /*Optional<Map<String, String>> opt = infoService.get("basic", new TypeReference<>() {});

        Map<String, String> config = opt.orElseGet(HashMap::new) ;
        System.out.println("config : " + config);*/

        BasicConfig config = infoService.get("basic", BasicConfig.class).orElseGet(BasicConfig::new) ;

        return config ;
    }
}
