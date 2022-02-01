package co.mr.jpaDemo02.dto;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

// 회원 가입화면으로 부터 넘어오는 가입정보를 담을 dto를 생성
@Getter @Setter
public class MemberFormDto {
    @NotBlank(message = "* 필수 입력값") // null 체크, 문자열의 경우 길이 0 및 문자열이 " " 검사
    private String name;

    @NotEmpty(message = "* 필수 입력값")
    @Email(message = "이메일 형식에 맞게 입력하세요.")
    private String email;

    @NotEmpty(message = "* 필수 입력값")
    @Length(min=8, max=20, message = "8자이상, 20자 이하로 입력하세요.")
    private String password;

    @NotEmpty(message = "* 필수 입력값")
    private String address;
}


