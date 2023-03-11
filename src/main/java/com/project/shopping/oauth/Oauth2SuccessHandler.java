package com.project.shopping.oauth;

import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import com.project.shopping.security.TokenProvider;
import com.project.shopping.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class Oauth2SuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRequstMapper userRequstMapper;

    @Autowired
    private  UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Oauth 로그인 성공");

        String accessToken = tokenProvider.generateAccessToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        // 쿠키 생성  후 쿠키 저장
        Cookie cookie = generatedCookie(refreshToken);
        response.addCookie(cookie);

        // Oauth 정보로 부터 email 및 name 추출
        String email = UserEmailGetByOauthInfo(authentication);
        String name = UserNameGetByOauthInfo(authentication);



        if(!userService.existsByEmail(email)){
            userService.OauthLoginCreateUser(email,name);
            response.sendRedirect( makeRediretjoinUrl(accessToken));

        }else{
            response.sendRedirect(makeRedirectUrl(accessToken));
        }


    }

    private String makeRediretjoinUrl(String token) {
        return UriComponentsBuilder.fromUriString("https://hannam.shop/registration/"+token)
                .build().toUriString();
    }

    private String makeRedirectUrl(String jwttoken) {
        return UriComponentsBuilder.fromUriString("https://hannam.shop/oauth/"+jwttoken)
                .build().toUriString();
    }

    private String UserEmailGetByOauthInfo(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");


        return email;
    }

    //Oauth 로 부터 받은 정보로 name 추출
    private String UserNameGetByOauthInfo(Authentication authentication){
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String name = (String) oAuth2User.getAttributes().get("name");


        return name;
    }

    private Cookie generatedCookie(String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        return  cookie;
    }



}
