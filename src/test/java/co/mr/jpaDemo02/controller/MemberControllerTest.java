package co.mr.jpaDemo02.controller;

import co.mr.jpaDemo02.dto.MemberFormDto;
import co.mr.jpaDemo02.entity.Member;
import co.mr.jpaDemo02.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc // MockMvc 가짜 객체를 이용한 테스트를 위해 선언
@TestPropertySource(locations = "classpath:application02.properties")
class MemberControllerTest {

    @Autowired
    private MemberService memberService;

    // mockMvc를 이용하면 웹브라우저에서 요청하는 것처럼 요청을 할 수 있다.
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 테스트를 위한 회원등록 메소드
    public Member createMember(String email, String password) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail(email);
        memberFormDto.setName("홍길서");
        memberFormDto.setAddress("대전 서구 둔산동");
        memberFormDto.setPassword(password);
        Member member = Member.createMember(memberFormDto, passwordEncoder);
        return memberService.saveMember(member);
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    public void loginSuccessTest() throws Exception{
        String email = "test@gmail.com";
        String password = "1234";
        this.createMember(email, password);

        // 가입된 회원정보로 로그인이 되는지 userParameter를 이용하여 테스트 한다.
        // 이메일을 아이디로 셋팅
        mockMvc.perform(formLogin().userParameter("email")
               .loginProcessingUrl("/members/login") // 로그인 url에 요청
               .user(email).password(password))
                // 로그인이 성공하여 인증되었는지 확인
               .andExpect(SecurityMockMvcResultMatchers.authenticated());
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    public void loginFailTest() throws Exception{
        String email = "test@gmail.com";
        String password = "1234";
        this.createMember(email, password);

        // 가입된 회원정보로 로그인이 되는지 userParameter를 이용하여 테스트 한다.
        // 이메일을 아이디로 셋팅
        mockMvc.perform(formLogin().userParameter("email")
                        .loginProcessingUrl("/members/login") // 로그인 url에 요청
                        .user(email)
                        .password("test"))
                // 인증되지 않은 결과값이 나오는지
                .andExpect(SecurityMockMvcResultMatchers.unauthenticated());
    }
}