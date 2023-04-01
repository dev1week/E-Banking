package com.example.bank_service.Handler.Aop;

import com.example.bank_service.Exception.CustomValidationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect
public class CustomValidationAdvice {
    //유효성 검사는 body가 있는 곳에서 하면된다.
        //때문에 포인트컷을 post와 put 매핑을 할 때로 정하였다.

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping(){}

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping(){}


    //유효성 검사 aop로 따로 빼놓았음
    @Around("postMapping() || putMapping()")
    public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        Object[] args = proceedingJoinPoint.getArgs(); // jointPoint의 매개변수
        for(Object arg : args){

            //valid 통과를못해서 bindingResult에 오류가 있을 경우 예외를 던진다.
            if(arg instanceof BindingResult){
                BindingResult bindingResult = (BindingResult) arg;
                if(bindingResult.hasErrors()){
                    Map<String, String> errorMap = new HashMap<>();

                    for(FieldError error : bindingResult.getFieldErrors()){
                        errorMap.put(error.getField(), error.getDefaultMessage());
                    }
                    throw new CustomValidationException("유효성 검사 실패", errorMap);
                }
            }
        }
        //정상적 메서드 실행
        return proceedingJoinPoint.proceed();
    }
}
