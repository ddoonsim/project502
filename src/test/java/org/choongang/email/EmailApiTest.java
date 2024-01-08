package org.choongang.email;

import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailApiTest {    // API 통합 테스트
    @Autowired
    private MockMvc mockMvc ;

    @Test
    @DisplayName("이메일 인증 코드 발급 및 검증 테스트")
    void sendVerifyEmailTest() throws Exception {
        // 인증 코드 발급 테스트
        HttpSession session = mockMvc.perform(get("/api/email/verify?email=2sujg97@naver.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getRequest().getSession() ;
        Integer authNum = (Integer) session.getAttribute("EmailAuthNum") ;

        // 인증 코드 검증 테스트
        mockMvc.perform(get("/api/email/auth_check?authNum=" + authNum))
                .andDo(print()) ;
    }
}
