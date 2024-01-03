package org.choongang.admin.menus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Menu {
    private final static Map<String, List<MenuDetail>> menus ;

    static {
        menus = new HashMap<>() ;
        // 주 메뉴 1
        menus.put("member", Arrays.asList(
                new MenuDetail("list", "회원목록", "/admin.member"),     // 서브 메뉴 1
                new MenuDetail("authority", "회원권한", "/admin/member/authority")    // 서브 메뉴 2
        ));
        // 주 메뉴 2
        menus.put("board", Arrays.asList(
                new MenuDetail("list", "게시판 목록", "/admin/board"),    // 서브 메뉴 1
                new MenuDetail("add", "게시판 등록", "/admin/board/add"),    // 서브 메뉴 2
                new MenuDetail("posts", "게시글 관리", "/admin/board/posts")    // 서브 메뉴 3
        ));
    }

    /**
     * 주 메뉴 코드로 서브 메뉴 리스트 반환
     * @param code : 주 메뉴 코드
     */
    public static List<MenuDetail> getMenus(String code) {
        return menus.get(code) ;
    }
}
