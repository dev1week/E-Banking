package com.example.bank_service.Dto.User;

import com.example.bank_service.Domain.User.User;
import com.example.bank_service.Domain.User.UserEnum;
import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserReqDto {

    @Data
    public static class JoinReqDto{
        //to-do 유효성 검사해야함
        @Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message="영문과 숫자로만 구성된 2~20자 이내의 닉네임을 사용해주세요")
        @NotEmpty
        private String username;
        @NotEmpty
        @Size(min=4,max=20)
        private String password;
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z0-9]{2,10}@[a-zA-Z0-9]{2,6}\\.[a-zA-Z]{2,3}$", message = "유효한 이메일 형식으로 작성해주세요")

        private String email;
        @NotEmpty
        @Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글과 영문으로만 구성된 1~20자 이내의 이름을 작성해주세요")
        private String fullname;

        public User toEntity(BCryptPasswordEncoder passwordEncoder){
            return User.builder()
                    .username(username)
                    .password(passwordEncoder.encode(password))
                    .email(email)
                    .fullname(fullname)
                    .role(UserEnum.CUSTOMER)
                    .build();
        }
    }
}
