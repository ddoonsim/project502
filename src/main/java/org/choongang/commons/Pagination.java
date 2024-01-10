package org.choongang.commons;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class Pagination {

    private int page ;
    private int total ;
    private int ranges ;
    private int limit ;
    private int firstRangePage ;    // 구간 별 첫 번호
    private int lastRangePage ;    // 구간 별 마지막 번호
    private int prevRangePage ;    // 이전 구간 첫 번호
    private int nextRangePage ;    // 다음 구간 첫 번호
    private int totalPages ;    // 전체 페이지 개수
    private String baseURL ;    // 페이징 쿼리스트링의 기본 URL

    /**
     *
     * @param page : 현재 페이지
     * @param total : 전체 레코드 개수 --> 총 페이지 개수 파악
     * @param ranges : 페이지 구간 개수
     * @param limit : 한 페이지 당 레코드 개수
     * @param request : 요청에 담긴 데이터 --> getQueryString()메서드로 쿼리스트링을 이용하기 위해 필요
     */
    public Pagination(int page, int total, int ranges, int limit, HttpServletRequest request) {
        page = Utils.onlyPositiveNumber(page, 1) ;
        total = Utils.onlyPositiveNumber(total, 0) ;
        ranges = Utils.onlyPositiveNumber(ranges, 10) ;
        limit = Utils.onlyPositiveNumber(limit, 20) ;

        // 총 페이지 개수
        int totalPages = (int) Math.ceil(total / (double)limit) ;

        // 구간 번호
        int rangeCnt = (page - 1) / ranges ;
        int firstRangePage = rangeCnt * ranges + 1 ;    // 구간의 첫 번호
        int lastRangePage = firstRangePage + ranges -1 ;    // 구간의 마지막 번호
        lastRangePage = lastRangePage > totalPages ? totalPages : lastRangePage ;

        // 이전 구간 첫 번호
        if (rangeCnt > 0) {
            prevRangePage = firstRangePage - ranges ;
        }

        int lastRangeCnt = (totalPages - 1) / ranges ;    // 마지막 구간 번호
        // 다음 구간 첫 번호
        if (rangeCnt < lastRangeCnt) {    // 마지막 구간이 아닌 경우
            nextRangePage = firstRangePage + ranges ;
        }

        /*
         * 쿼리스트링 값 유지 처리 : 쿼리스트링 값 중 page만 제외하고 다시 조합
         * ex) ?orderStatus=CACH&name=...&page=2 --> ?orderStatus=CACH&name=...
         *     ?page=2 --> ?
         *     없는 경우 --> ?
         *
         * 1. '&' 기호를 기준으로 쪼개서 문자열 배열로 변환
         * 2. page=n을 제외한 "키=값" 요소들을 연결하여 하나의 문자열로 변환
         */
        String baseURL = "?" ;
        if (request != null) {
            String queryString = request.getQueryString();    // 쿼리스트링 가져오기
            if (StringUtils.hasText(queryString)) {
                queryString = queryString.replace("?", "") ;
                baseURL += Arrays.stream(queryString.split("&"))
                            .filter(s -> !s.contains("page="))    // page= 건너뛰기
                            .collect(Collectors.joining("&"));    // 문자열 배열의 요소들을 연결하여 하나의 문자열로 변환

                baseURL = baseURL.length() > 1 ? baseURL += "&" : baseURL ;
            }
        }

        this.page = page ;
        this.total = total ;
        this.ranges = ranges ;
        this.limit = limit ;
        this.firstRangePage = firstRangePage ;
        this.lastRangePage = lastRangePage ;
        this.totalPages = totalPages ;
        this.baseURL = baseURL ;
    }

    public Pagination(int page, int total, int ranges, int limit) {
        this(page, total, ranges, limit, null) ;
    }

    public List<String[]> getPages() {
        // 0 : 페이지 번호, 1: 페이지 url --> ?page=페이지번호 쿼리스트링 자동 완성
        return IntStream.rangeClosed(firstRangePage, lastRangePage)
                .mapToObj(p -> new String[] { String.valueOf(p), baseURL + "page=" + p }).toList() ;
    }
}
