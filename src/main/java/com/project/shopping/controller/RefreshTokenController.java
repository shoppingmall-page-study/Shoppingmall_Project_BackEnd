package com.project.shopping.controller;


import com.project.shopping.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class RefreshTokenController {
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/api/reissuance/refreshToken")
    public ResponseEntity<?> reIssuanceRefreshToken(@CookieValue(value = "refreshToken") String refreshToken){

        HttpHeaders headers = refreshTokenService.reIssuanceRefreshToken(refreshToken);

        return ResponseEntity.ok().headers(headers).body("토큰이 재발급 되었습니다");

    }

    // logout 시 cookie 저장된 refreshToken 삭제
    @DeleteMapping("/api/delete/refreshToken")
    public ResponseEntity<?> expireRefreshToken( HttpServletResponse response, Authentication authentication){
        refreshTokenService.deleteRefreshToken(response);
        return ResponseEntity.ok().body("refreshToken이 삭제 완료 되었습니다");
    }
}
