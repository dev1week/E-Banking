package com.example.bank_service.Config.dummy;

import com.example.bank_service.Domain.User.User;
import com.example.bank_service.Domain.User.UserEnum;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class DummyObject {
    //리포지토리까지 테스트할 때 사용할 객체
        //id와 date time은 data jpa에서 알아서 처리하기 때문에 안넣어도 된다.
    protected User newUser(String username, String fullname){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .username(username)
                .password(encPassword)
                .email(username+"@naver.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .build();
    }
    //테스트 안에서 리포지토리를 사용하지 않을 경우 객체
        //id와 date time은 data jpa에서 알아서 처리하기 때문에 repository를 경유하지 않을 경우 id와 datetime을 수동으로 넣어줘야한다.
    protected User newMockUser(Long id, String username, String fullname){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("1234");
        return User.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username+"@naver.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
