package com.example.bank_service.Service;

import com.example.bank_service.Config.dummy.DummyObject;
import com.example.bank_service.Domain.User.User;
import com.example.bank_service.Domain.User.UserRepository;
import com.example.bank_service.Dto.User.UserReqDto.JoinReqDto;
import com.example.bank_service.Dto.User.UserRespDto.JoinResDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest extends DummyObject {
    //1.모키토를 사용하기 때문에 스프링 관련 빈들이 존재하지 않는다.
    @InjectMocks
    private UserService userService;
    //2. 때문에 리포지토리도 같이 주입시켜야한다.
    @Mock
    private UserRepository userRepository;

    //진짜 빈에 있는 것을 꺼내와서 목 환경에 넣어줌
    @Spy
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void 중복검사를통과한회원가입() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@naver.com");
        joinReqDto.setFullname("test");

        //stub1.
            //mock 환경이기 때문에 userRepository를 di하지 못하므로 해당 클래스 내 메서드가 호출되었을 경우 대신 사용하는 코드를 작성함
            //유저 이름이 중복되지 않았을 경우를 가정하여 null을 꺼내온다.
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());



        //stub2.
            //중복검사를 통과했기 때문에 repository에 저장되었다는 것 상정하여 저장된 객체를 생성함
        User test = newMockUser(1L, "test", "test");

        when(userRepository.save(any())).thenReturn(test);

        //when
         JoinResDto joinResDto = userService.join(joinReqDto);

         //then
        assertThat(joinResDto.getId()).isEqualTo(1L);
        assertThat(joinResDto.getUsername()).isEqualTo("test");
        System.out.println(joinResDto.toString());
    }
    @Test
    public void 중복검사를통과하지못한회원가입() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@naver.com");
        joinReqDto.setFullname("test");

        //stub1.
            //유저 이름이 중복되었을 경우에는 예외가 터진다.
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));

        try {
            //when
            JoinResDto joinResDto = userService.join(joinReqDto);
        } catch (RuntimeException e) {
            //then
            Assertions.assertEquals("동일한 username이 존재합니다", e.getMessage());
        }

    }

    

}