package com.example.bank_service.Config;

import com.example.bank_service.Domain.User.UserEnum;
import com.example.bank_service.Dto.ResponseDto;
import com.example.bank_service.Util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


@Configuration
public class SecurityConfig {

    private final Logger log = LoggerFactory.getLogger(getClass()); //@slf4j 대신에 사용한다.

    @Bean // Ioc 컨테이너에 BCryptPasswordEncoder() 객체가 등록됨.
    public BCryptPasswordEncoder passwordEncoder() {
        log.debug("디버그 : BCryptPasswordEncoder 빈 등록됨");
        return new BCryptPasswordEncoder();
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.debug("디버그 : filterChain 빈 등록됨");
        // iframe 허용안함.
        http.headers().frameOptions().disable();
        http.csrf().disable();
        http.cors().configurationSource(configurationSource());

        //jwt 사용 => 세션 사용안함
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        // rest api 형태로 만들 것이기 때문에 form login은 필요 없다. => from login 비활성화
        http.formLogin().disable();
        // httpBasic은 브라우저가 팝업창을 이용해서 사용자 인증을 진행한다. => 비활성화
        http.httpBasic().disable();


        http.authorizeRequests()
                .antMatchers("/api/s/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN)
                .anyRequest().permitAll();

        //예외 가로채서 공통 응답 dto 만들기
        //#7 -1 상태에서는 응답 양식이 자기 멋대로 내려감 통일성이 없기 때문에 통일성을 만들어준다.
            //403이 아닌 401을 반환해야한다.
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            CustomResponseUtil.Unauthentication(response, "로그인을 진행해주세요");
        });

        return http.build();
    }

    public CorsConfigurationSource configurationSource() {
        log.debug("디버그 : configurationSource cors 설정이 SecurityFilterChain에 등록됨");
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*"); // GET, POST, PUT, DELETE (Javascript 요청 허용)
        configuration.addAllowedOriginPattern("*"); // 모든 IP 주소 허용
        configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
        configuration.addExposedHeader("Authorization");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
