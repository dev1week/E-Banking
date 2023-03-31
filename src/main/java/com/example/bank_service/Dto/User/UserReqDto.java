package com.example.bank_service.Dto.User;

import com.example.bank_service.Domain.User.User;
import com.example.bank_service.Domain.User.UserEnum;

import lombok.Data;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class UserReqDto {
    @Data
    public static class JoinReqDto{
        //to-do 유효성 검사해야함
        private String username;
        private String password;
        private String email;
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
