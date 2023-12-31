package org.choongang.admin.member.controllers;

import lombok.RequiredArgsConstructor;
import org.choongang.admin.menus.Menu;
import org.choongang.admin.menus.MenuDetail;
import org.choongang.commons.ExceptionProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller("adminMemberController")
@RequestMapping("/admin/member")
@RequiredArgsConstructor
public class MemberController implements ExceptionProcessor {

    @ModelAttribute("menuCode")
    public String getMenuCode() {
        return "member" ;
    }


    @ModelAttribute("pageTitle")
    public String getPageTitle() {
        return "회원관리" ;
    }

    /**
     * member의 하위 메뉴를 뷰 페이지에 전달
     */
    @ModelAttribute("subMenus")
    public List<MenuDetail> getSubMenus() {

        return Menu.getMenus("member") ;
    }


    @GetMapping
    public String list(Model model) {
        model.addAttribute("subMenuCode", "list") ;

        return "admin/member/list" ;
    }
}
