package co.mr.jpaDemo02.service;

import co.mr.jpaDemo02.dto.MemberFormDto;
import co.mr.jpaDemo02.entity.Member;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
// 테스트클래스에 @Transactional을 선언하면 롤백처리되어 실제 반영이 되지 않는다.
// 따라서, 같은 메소드를 반복해서 테스트해 볼 수 있다.
@Transactional
@TestPropertySource(locations = "classpath:application02.properties")
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

    // 회원정보를 입력한 테스트용 member 엔티티 만들기
    public Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setEmail("test@naver.com");
        memberFormDto.setName("홍길동");
        memberFormDto.setAddress("대전시 서구 둔산동");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    @DisplayName("회원 가입 테스트")
    public void saveMemberTest() {

        Member member = createMember();
        Member savedMember = memberService.saveMember(member);

        assertEquals(member.getEmail(), savedMember.getEmail());
        assertEquals(member.getName(), savedMember.getName());
        assertEquals(member.getAddress(), savedMember.getAddress());
        assertEquals(member.getPassword(), savedMember.getPassword());
        assertEquals(member.getRole(), savedMember.getRole());
    }

    @Test
    @DisplayName("중복회원 가입테스트")
    public void savedDuplicateMemberTest() {
        Member member01 = createMember();
        Member member02 = createMember();

        memberService.saveMember(member01);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            memberService.saveMember(member02);
        });

        assertEquals("이미 가입된 회원입니다!!", exception.getMessage());
    }
}

//    Exception의 종류
//
//      IllegalArgumentException: 메소드의 전달인자 값이 부적절한 경우 발생.
//        -> Illegal(부적절한) Argument(아규먼트) Exception(예외)
//        참고 링크: http://bufferoverflow.tistory.com/entry/파라미터-parameter-아규먼트-argument
//
//        IllegalStateException: 객체의 상태가 메소드 호출에는 부적합한 경우 발생.
//        -> Illegal(부적절한) State(상태) Exception(예외상황)
//        ex> java.lang.IllegalStateException: getOutputStream() has already been called for this response
//        -> 해당 response는 getOutputStream() 메소드를 호출하기 위한 준비가 되어있지 않습니다.
//
//        NullPointerException: null 이 금지된 상황에서 전달인자 값이 null인 경우 발생한다.
//        -> Null(null) Pointer(포인터) Exception(예외상황)
//
//        IndexOutOfBoundsException: index 값이 범위를 벗어난 경우 발생한다.
//        -> Index(인덱스) OutOfBounds(범위이탈) Exception(예외상황)
//
//        ConcurrentModificationException: 금지된 곳에서 객체를 동시에 수정(concurrent modification)하는 것이 감지된 경우 발생한다.
//        -> Concurrent(동시) Modification(수정) Exception(예외상황)
//        참고 링크 : http://wonsama.tistory.com/194
//
//        UnsupportedOperationException: 객체가 메소드를 지원하지 않는 경우 발생한다.
//        -> Unsupported(지원하지 않는) Operation(객체) Exception(예외상황)
//        참고 링크: http://younghoe.info/482

