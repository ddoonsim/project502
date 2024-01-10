package org.choongang.member.controllers;

import lombok.Data;

/**
 * 멤버 조회에 관련된 커맨드 객체
 */
@Data
public class MemberSearch {

    private int page = 1 ;    // 현재 페이지 번호
    private int limit = 20 ;    // 한 페이지 당 노출할 레코드 개수
}
