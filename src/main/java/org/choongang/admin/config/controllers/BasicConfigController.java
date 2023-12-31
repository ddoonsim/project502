package org.choongang.admin.config.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.config.service.ConfigInfoService;
import org.choongang.admin.config.service.ConfigSaveService;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/config")
@RequiredArgsConstructor
public class BasicConfigController implements ExceptionProcessor {

    private final ConfigSaveService saveService ;
    private final ConfigInfoService infoService ;

    @ModelAttribute("menuCode")
    // 이 컨트롤러 내부, 전역에서 유지하여 뷰 페이지로 전달할 데이터 ==> @ModelAttribute("attributeName")
    public String getMenuCode() {
        return "config" ;
    }

    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "기본설정" ;
    }

    @GetMapping
    public String index(Model model) {
        // 데이터 조회 : BasicConfig 객체 호출, 없으면 생성
        BasicConfig config = infoService.get("basic", BasicConfig.class)
                                        .orElseGet(BasicConfig::new) ;

        model.addAttribute("basicConfig", config) ;

        return "admin/config/basic" ;
    }

    @PostMapping
    public String save(BasicConfig config, Model model) {

        saveService.save("basic", config);    // 데이터 저장

        model.addAttribute("message", "✅ 저장되었습니다.") ;

        return "admin/config/basic" ;
    }
}
