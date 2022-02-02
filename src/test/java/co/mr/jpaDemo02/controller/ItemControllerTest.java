package co.mr.jpaDemo02.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application02.properties")
class ItemControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    @DisplayName("Admin 권한 테스트")
    //회원의 이름 admin, role은 ADMIN인 유저가 로그인된 상태로 테스트할 수 있도록 해주는 어노테이션
    @WithMockUser(username = "admin", roles = "ADMIN")
    public void itemFormTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))// 상품 등록 페이지에 get요청
                .andDo(print()) // 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력
                .andExpect(status().isOk()); // 응답 상태코드가 정상인지 확인
    }


    @Test
    @DisplayName("ADMIN 권한 접근 금지 테스트")
    //회원의 이름 admin, role은 ADMIN인 유저가 로그인된 상태로 테스트할 수 있도록 해주는 어노테이션
    @WithMockUser(username = "admin", roles = "USER")
    public void itemFormUserTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/admin/item/new"))// 상품 등록 페이지에 get요청
                .andDo(print()) // 요청과 응답 메시지를 확인할 수 있도록 콘솔창에 출력
                .andExpect(status().isForbidden()); // 응답 상태코드가 정상인지 확인
    }
}