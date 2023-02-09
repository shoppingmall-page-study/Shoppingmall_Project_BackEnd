package com.project.shopping.controller;


import com.project.shopping.security.Token;
import com.project.shopping.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/api/reissuance/refreshToken")
    public ResponseEntity<?> reissuanceRefreshToken(@CookieValue(value = "refreshToken") String refreshTooken, HttpServletRequest request, HttpServletResponse response){
        Token reissuanceToekn = refreshTokenService.reissuanceRefreshToken(request,refreshTooken);
        String reissuanceAccessToken = reissuanceToekn.getAccessToken();
        String reissuanceRefreshToken = reissuanceToekn.getRefreshToken();

        //헤더에 accessToken 보내기
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+reissuanceAccessToken);

        // refreshToken  cookie 로 보내기
        Cookie cookie =  new Cookie("refreshToken",reissuanceRefreshToken);

        cookie.setPath("/");
        response.addCookie(cookie);
        return ResponseEntity.ok().headers(headers).body("토큰이 재발급 되었습니다");

    }
    @GetMapping("/deleteCookie")
    public String deleteCookies(HttpServletResponse servletResponse){
        Cookie cookie = new Cookie("refreshToken","null");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        servletResponse.addCookie(cookie);
        return "cookie 삭제 ";
    }
}
