package co.mr.jpaDemo02.service;

import co.mr.jpaDemo02.entity.Member;
import co.mr.jpaDemo02.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

// UserDetailsService 인터페이스 : 데이터베이스에서 회원정보를 가져오는 역할
//      loadUserByUserUsernames() 메소드가 추상화 되어있음. 구현 후 UserDetails를 반환


@Service
@Transactional
@RequiredArgsConstructor

public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member) {
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    private void validateDuplicateMember(Member member) {
        Member findMember = memberRepository.findByEmail(member.getEmail());

        if (findMember != null) {
            throw new IllegalStateException("이미 가입된 회원입니다!!");
        }
    }

    // UserDetails 인터페이스 : 회원정보를 담기위해 사용하는 인터페이스
    // 스프링 시큐리티는 UserDetails를 구현한 User클래스를 제공하고 있다.

    // 스프링 시큐리티에서는 UserDetailsService를 구현하고 있는 클래스를 통해서 로그인을 구현한다.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException(email);
        }
        return User.builder()
                .username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }
}
