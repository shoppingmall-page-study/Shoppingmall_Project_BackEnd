package com.project.shopping.oauth;

import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.model.User;
import com.project.shopping.security.TokenProvider;
import com.project.shopping.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import java.util.Collections;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService {


    private final UserService userService;



    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // oauth 로 부터 받은 access 토큰으로 부터 정보 추출
        OAuth2Attribute oAuth2Attribute = oAuth2Attribute(userRequest);
        log.info("{}", oAuth2Attribute);

        // email, name 추출
        String name = getNameToOauth(oAuth2Attribute);
        String email = getEmailToOauth(oAuth2Attribute);
        log.info("oath 추출 이름" + name);
        log.info("oath 추출 email " + email);


        User user = !userService.existsByEmail(email) ? oAuthLoginCreateUser(email,name) : oAuthLoginFindByEmail(email);

        return new PrincipalDetails(user,oAuth2Attribute.getAttributes());
    }



    //Oauth  정보 추출
    private  OAuth2Attribute oAuth2Attribute(OAuth2UserRequest userRequest){
        OAuth2UserService<OAuth2UserRequest,OAuth2User> oAuth2UserService= new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);
        String registrationId =userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
        return OAuth2Attribute.of(registrationId,userNameAttributeName,oAuth2User.getAttributes());
    }

    // Oauth 정보에서 이름 추출
    private  String getNameToOauth(OAuth2Attribute oAuth2Attribute){
        return oAuth2Attribute.getName();
    }

    //Oauth 정보에서 이메일 추출
    private String getEmailToOauth(OAuth2Attribute oAuth2Attribute){
        return  oAuth2Attribute.getEmail();
    }


    // Oauth 첫 로그인시 User 생성
    private User oAuthLoginCreateUser(String email, String name){
        log.info("Oauth 첫 로그인");
        return  userService.oAuthLoginCreateUser(email,name);

    }
    // Oauth 로그인시 User 존재할시 email 부터 user 찾기
    private User oAuthLoginFindByEmail(String email){
        log.info("이미 기존 회원 있음");
        return userService.findByEmail(email);
    }





}
