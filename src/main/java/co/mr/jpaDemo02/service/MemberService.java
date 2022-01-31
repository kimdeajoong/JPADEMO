package co.mr.jpaDemo02.service;

import co.mr.jpaDemo02.entity.Member;
import co.mr.jpaDemo02.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional // 로직 처리중 에러가 발생하면 rollBack처리

// @RequiredArgsConstructor 어노테이션은
// final이나 @NonNull이 붙은 필드를 주입하는 생성자를 생성해준다.
// 생성자 주입을 해주는 어노테이션이라고 생각하면됨
// 참고>
// @NonNull 어노테이션을 변수에 붙이면 자동으로 null 체크를 해줍니다.
// 즉, 해당 변수가 null로 넘어온 경우, NullPointerException 예외를 일으켜 줍니다.

@RequiredArgsConstructor

public class MemberService {
    private final MemberRepository memberRepository;

//    @RequiredArgsConstructor이 아래와 같이 생성자 주입을 해줌
//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

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

}
