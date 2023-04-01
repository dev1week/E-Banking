package com.example.bank_service.Controller;

import com.example.bank_service.Config.dummy.DummyObject;
import com.example.bank_service.Domain.User.UserRepository;
import com.example.bank_service.Dto.ResponseDto;
import com.example.bank_service.Dto.User.UserReqDto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class UserControllerTest extends DummyObject {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp(){
        dataSetting();
    }

    private void dataSetting()
    {
        userRepository.save(newUser("overlap", "중복됨"));
    }
    @Test
    public void 회원가입정상() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@naver.com");
        joinReqDto.setFullname("테스트");

        //만들어둔 회원가입 요청 dto를 json으로 매핑
        String requestBody = om.writeValueAsString(joinReqDto);


        //when
            //json 바디 데이터를 담아서 api/join 호출
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
            //결과값의  body를 담아와서 출력해본다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();

        //then
        resultActions.andExpect(status().isCreated());

    }

    @Test
    public void 회원가입실패_이미동일이름회원존재() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("overlap");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("overlap@naver.com");
        joinReqDto.setFullname("중복됨");

        //만들어둔 회원가입 요청 dto를 json으로 매핑
        String requestBody = om.writeValueAsString(joinReqDto);


        //when
        //json 바디 데이터를 담아서 api/join 호출
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //결과값의  body를 담아와서 출력해본다.


        //then
        resultActions.andExpect(status().isBadRequest());
    }

    @Test
    public void 회원가입실패_유효하지않은email_어퍼스토리피없는경우() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("testnaver.com");
        joinReqDto.setFullname("테스트");

        //만들어둔 회원가입 요청 dto를 json으로 매핑
        String requestBody = om.writeValueAsString(joinReqDto);


        //when
        //json 바디 데이터를 담아서 api/join 호출
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //결과값의  body를 담아와서 출력해본다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        ResponseDto response = om.readValue(responseBody, ResponseDto.class);
        System.out.println(response.getData());
        //then
        resultActions.andExpect(status().isBadRequest());

        assertThat("{email=유효한 이메일 형식으로 작성해주세요}").isEqualTo(response.getData().toString());
    }

    @Test
    public void 회원가입실패_유효하지않은닉네임() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("testㅇㄹㅇㄹ");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@naver.com");
        joinReqDto.setFullname("테스트");

        //만들어둔 회원가입 요청 dto를 json으로 매핑
        String requestBody = om.writeValueAsString(joinReqDto);


        //when
        //json 바디 데이터를 담아서 api/join 호출
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //결과값의  body를 담아와서 출력해본다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);
        ResponseDto response = om.readValue(responseBody, ResponseDto.class);
        System.out.println(response.getData());
        //then
        resultActions.andExpect(status().isBadRequest());

        assertThat("{username=영문과 숫자로만 구성된 2~20자 이내의 닉네임을 사용해주세요}").isEqualTo(response.getData().toString());
    }

    @Test
    public void 회원가입실패_유효하지않은짧은비밀번호() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1");
        joinReqDto.setEmail("test@naver.com");
        joinReqDto.setFullname("테스트");

        //만들어둔 회원가입 요청 dto를 json으로 매핑
        String requestBody = om.writeValueAsString(joinReqDto);


        //when
        //json 바디 데이터를 담아서 api/join 호출
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //결과값의  body를 담아와서 출력해본다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);
        ResponseDto response = om.readValue(responseBody, ResponseDto.class);
        //System.out.println(response.getData());
        //then
        resultActions.andExpect(status().isBadRequest());

        assertThat("{password=size must be between 4 and 20}").isEqualTo(response.getData().toString());
    }

    @Test
    public void 회원가입실패_유효하지않은이름() throws Exception{
        //given
        JoinReqDto joinReqDto = new JoinReqDto();
        joinReqDto.setUsername("test");
        joinReqDto.setPassword("1234");
        joinReqDto.setEmail("test@naver.com");
        joinReqDto.setFullname("123");

        //만들어둔 회원가입 요청 dto를 json으로 매핑
        String requestBody = om.writeValueAsString(joinReqDto);


        //when
        //json 바디 데이터를 담아서 api/join 호출
        ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
        //결과값의  body를 담아와서 출력해본다.
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        //System.out.println(responseBody);
        ResponseDto response = om.readValue(responseBody, ResponseDto.class);
        //System.out.println(response.getData());
        //then
        resultActions.andExpect(status().isBadRequest());

        assertThat("{fullname=한글과 영문으로만 구성된 1~20자 이내의 이름을 작성해주세요}").isEqualTo(response.getData().toString());
    }

}