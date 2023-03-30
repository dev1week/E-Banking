package com.example.bank_service.Util;

import com.example.bank_service.Dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomResponseUtil {
    private static final Logger log = LoggerFactory.getLogger(CustomResponseUtil.class); //@slf4j 대신에 사용한다.
    public static void Unauthentication(HttpServletResponse response, String msg){

        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            //객체를 json으로 변환
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset = utf-8");
            response.setStatus(401);
            response.getWriter().println(responseBody);
        } catch (IOException e) {
            log.error("파싱 에러");
        }
    }
}
