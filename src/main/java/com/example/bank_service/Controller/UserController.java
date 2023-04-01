package com.example.bank_service.Controller;

import com.example.bank_service.Dto.ResponseDto;
import com.example.bank_service.Dto.User.UserReqDto.JoinReqDto;
import com.example.bank_service.Dto.User.UserResDto.JoinResDto;
import com.example.bank_service.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class UserController {

    private final UserService userService;
    @PostMapping("/join")
   public ResponseEntity<?> mehtod(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult){


        //유효성 검사 실패시 에러 dto 반환 로직


        JoinResDto joinResDto = userService.join(joinReqDto);
        return new ResponseEntity<>(new ResponseDto<>(1,"회원가입 성공",joinResDto), HttpStatus.CREATED);
   }

}
