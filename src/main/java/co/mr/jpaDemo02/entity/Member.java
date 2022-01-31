package co.mr.jpaDemo02.entity;

import co.mr.jpaDemo02.constant.Role;
import co.mr.jpaDemo02.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    // email은 nnique하게 설정, 동일한 email이 들어올 수 없도록
    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    // enum타입을 엔티티속성으로 지정할 수 있다.
    @Enumerated(EnumType.STRING)
    private Role role;

    // Member 엔티티를 생성하는 메소드
    // Member엔티티에 회원을 생성하는 메소드를 만들어서 관리를 하면 코드가 변경되더라도
    // 한군데만 수정할 수 있어 편리한다.
    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder) {
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        member.setAddress(memberFormDto.getAddress());
        // 스프링 시큐리티 설정 클래스에 등록 BCryptPasswordEncoder Bean을 파라미터로 넘겨서
        // 비밀번호를 암호화 한다.
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setRole(Role.USER);
        return member;
    }
}
