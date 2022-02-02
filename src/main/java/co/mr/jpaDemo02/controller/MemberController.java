package co.mr.jpaDemo02.controller;

import co.mr.jpaDemo02.dto.MemberFormDto;
import co.mr.jpaDemo02.entity.Member;
import co.mr.jpaDemo02.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@RequestMapping("/members")
@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/new")
    public String memberForm(Model model) {
        model.addAttribute("memberFormDto", new MemberFormDto());
        return "member/memberForm";
    }

    @PostMapping("/new")
    // 검증 대상 객체에 @Valid를 붙여주고, 검사결과를 담아놓을 BindingResult 객체를 파라미터로 추가
    public String memberForm(@Valid MemberFormDto memberFormDto,
                             BindingResult bindingResult, Model model) {
        // bindingResult.hasErrors()호출하여 에러가 있으면 가입페이지로 이동
        System.out.println("binddingResult 결과 : " + bindingResult.hasErrors());
        if (bindingResult.hasErrors()) {
            return "/member/memberForm";
        }

        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        } catch (IllegalStateException e) {
            // 에러 메시지를 뷰로 전달
            model.addAttribute("errorMessage", e.getMessage());
            return "/member/memberForm";
        }

        // 가입 성공하면 home으로
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginMember() {
        return "/member/memberLoginForm";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }

    @GetMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginErrorMsg", "아이디 또는 비밀번호를 확인하세요.");
        return "/member/memberLoginForm";
    }
}
