package com.example.bank_service.Service;

import com.example.bank_service.Domain.User.User;
import com.example.bank_service.Domain.User.UserRepository;
import com.example.bank_service.Dto.User.UserReqDto.JoinReqDto;
import com.example.bank_service.Dto.User.UserResDto;
import com.example.bank_service.Dto.User.UserResDto.JoinResDto;
import com.example.bank_service.Exception.CustomApiException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(getClass()); //@slf4j 대신에 사용한다.
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional // 메서드 시작시 트랜잭션 시작 메서드 종료시 트랜잭션 종료
    public JoinResDto join(JoinReqDto joinReqDto){
        //1. 동일 유저명 검사

        Optional<User> userOP = userRepository.findByUsername(joinReqDto.getUsername());
        if (userOP.isPresent()) {
            // 유저네임 중복되었다는 뜻
            throw new CustomApiException("동일한 username이 존재합니다");
        }

        //2 패스워드 인코딩
        User userPs = userRepository.save(joinReqDto.toEntity(passwordEncoder));
        //3.  dto 응답
        return new UserResDto.JoinResDto(userPs);
    }



}
