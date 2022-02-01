package co.mr.jpaDemo02.config;

import co.mr.jpaDemo02.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
// @EnableWebSecurity 애너테이션은 웹 보안을 활성화 한다.
// WebSecurityConfigurerAdapter를 상속받는 클래스에 @EnableWebSecurity를 선언하면
// SpringSecurityFilterChain이 자동으로 포함된다.

// 하지만 그자체로는 유용하지 않고, 스프링 시큐리티가 WebSecurityConfigurer를 구현하거나
// 컨텍스트의 WebSebSecurityConfigurerAdapter를 확장한 빈으로 설정되어 있어야 한다.
//
// 하지만 WebSebSecurityConfigurerAdapter를 확장하여
// 클래스를 설정하는 것이 가장 편하고 자주 쓰이는 방법이다.

// WebSecurityConfigurerAdapter에는 세부적인 보안설정과 관련된 API를 제공(커스터마이징 가능)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    MemberService memberService;

    // http요청에 대한 보안을 설정한다.
    // 페이지 권한설정, 로그인 페이지 설정, 로그아웃 메소드 등에 대한 설정 할 수 있다.

    // Security 의존성을 추가하면 모든 요청에 인증을 필요로 하지만
    // 이메소드에 설정을 추가하지 않으면 요청에 대한 인증을 요구하지 않는다.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin()
                .loginPage("/members/login") // 로그인 페이지 설정
                .defaultSuccessUrl("/") // 로그인 성공시 이동 페이지
                .usernameParameter("email") //로그인시 사용할 파라미터 이름으로 email지정
                .failureUrl("/members/login/error") // 로그인 실패시 이동할 URL설정
                .and()
                .logout()
                // 로그아웃 URL설정
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout"))
                .logoutSuccessUrl("/"); // 로그아웃 성공시 이동할 URL
    }

    // 비밀번호를 데이터베이스에 그대로 저장했을 경우, 데이터베이스가 해킹당했을 때
    // 고객의 회원정보가 그대로 노출되는 것을 막기 위해 BCryptPasswordEncoder의 해시함수를
    // 이용하여 비밀번호를 암호화하여 저장
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Spring Security에서 인증은 AuthenticationManager를 통해서 이루어진다.
    // AuthenticationManagerBuilder가 AuthenticationManager를 생성한다.
    // userDetailsService를 구현하고 있는 memberService객체를 인자로 지정
    // 비밀번호 암호화를 위해 passwordEncoder 지정
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService)
                .passwordEncoder(passwordEncoder());

    }

}
