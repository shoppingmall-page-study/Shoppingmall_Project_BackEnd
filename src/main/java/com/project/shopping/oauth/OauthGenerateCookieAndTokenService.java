package com.project.shopping.oauth;

import com.project.shopping.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;

@Service
@RequiredArgsConstructor
public class OauthGenerateCookieAndTokenService {
    private final TokenProvider tokenProvider;


    public Cookie generateCookie(String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setPath("/");
        return  cookie;
    }


    public String generateAccessToken(Authentication authentication){
        return tokenProvider.generateAccessToken(authentication);
    }

    public String generateRefreshToken(Authentication authentication){
        return tokenProvider.generateRefreshToken(authentication);
    }
}
