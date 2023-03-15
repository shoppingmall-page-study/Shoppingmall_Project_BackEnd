package com.project.shopping.oauth;

import com.project.shopping.security.TokenProvider;
import com.project.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//oauth 로그인 성공시 리다이렉트 시켜주는 클래스
@Component
@Slf4j
@RequiredArgsConstructor
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Oauth 로그인 성공");

        String accessToken = generateAccessToken(authentication);
        String refreshToken = generateRefreshToken(authentication);


        // 쿠키 생성  후 쿠키 저장
        response.addCookie(generateCookie(refreshToken));

        // 해당 url 로 리다이렉트
        response.sendRedirect(makeRedirectUrl(accessToken));


    }


    // 나중 이슈를 통한 구현

    //private String makeRedirectJoinUrl(String token) {
        //return UriComponentsBuilder.fromUriString("https://hannam.shop/registration/"+token)
                //.build().toUriString();
    //}

    private String makeRedirectUrl(String token) {
        return UriComponentsBuilder.fromUriString("https://hannam.shop/oauth/"+token)
                .build().toUriString();
    }

    //쿠키 생성

    private Cookie generateCookie(String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        return  cookie;
    }


    // access 토큰 생성

    private String generateAccessToken(Authentication authentication){
        return tokenProvider.generateAccessToken(authentication);
    }
    //refresh 토큰 생성

    private String generateRefreshToken(Authentication authentication){
        return tokenProvider.generateRefreshToken(authentication);
    }




}
