package co.mr.jpaDemo02.repository;

import co.mr.jpaDemo02.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    // 이메일 쿼리메소드
    // 회원 가입시 중복된 회원이 있는지 검사
    Member findByEmail(String email);
}
