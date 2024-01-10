package org.choongang.paging;

import jakarta.servlet.http.HttpServletRequest;
import org.choongang.commons.Pagination;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
public class PagingTest {

    @Mock    // mock데이터 만듦
    private HttpServletRequest request ;

    @Test
    @DisplayName("페이지 구간별 데이터 테스트")
    void pagingTest() {
        Pagination pagination = new Pagination(23, 12345, 5, 20) ;
        List<String[]> data = pagination.getPages() ;
        data.forEach(s -> System.out.println(Arrays.toString(s)));
        System.out.println(pagination);
    }

    @Test
    @DisplayName("페이징 쿼리스트링이 유지되는지 테스트")
    void pagingWithRequestTest() {
        given(request.getQueryString())    // mockup 객체를 만들어서 테스트
                .willReturn("?orderStatus=CACH&userNm=name&page=3") ;

        Pagination pagination = new Pagination(23, 12345, 5, 20, request) ;
        List<String[]> data = pagination.getPages() ;
        data.forEach(s -> System.out.println(Arrays.toString(s)));
    }
}
