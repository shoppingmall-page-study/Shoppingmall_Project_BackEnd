package com.project.shopping.security;


import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.oauth.CustomOAuth2UserService;
import com.project.shopping.oauth.Oauth2SuccessHandler;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.annotations.reflection.internal.XMLContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenProvider {
    private Key key;


    @Value("${jwt.secret}")
    private  String secretKey;

    @PostConstruct
    protected  void init(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }



    // 토큰으로 부터 email 출력
    public String TokenInfo(String token){
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }


    //유저 정보를 가지고 AccessToken 토큰 생성
    public String generateAccessToken(Authentication authentication){
        // 인증 객체로 eamil 생성
        String email = authenticationGetEmail(authentication);
        //권한 정보 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        //Access Token Refresh Token  만료 시간 정하기
        Date accessTokenExpiresIn = generateDateExpiresIn( 1000*60*60*24); // 만료 시간 // 하루
        String accessToken = Jwts.builder().setSubject(email).claim("auth",authorities).setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();

        return accessToken;
    }


    // refreshToken 생성

    public String generateRefreshToken(Authentication authentication){

        Date refreshTokenExpiresIn = generateDateExpiresIn( 1000*60*60*24*10); // refreshToken 만료시간 // 10일
        log.info(authentication.getClass().getName());
        // 이메일 정보 추출
        String email = authenticationGetEmail(authentication);

        // Refresh Token 생성
        String refreshToken = Jwts.builder().setSubject(email).setExpiration(refreshTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();
        log.info("refreshToken 생성",refreshToken);

        return refreshToken;
    }





    // 토큰 정보 검증 메소드
    public boolean validateToken(String token, HttpServletRequest request){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e){
            e.printStackTrace();
            log.info("exception",e);
            request.setAttribute("exception", ErrorCode.UnauthorizedException.getErrorCode());

        }catch (ExpiredJwtException e){
            e.printStackTrace();
            request.setAttribute("exception",ErrorCode.ExpirationException.getErrorCode());
            log.info("Expired Jwt Token",e);

        }catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token",e);
            request.setAttribute("exception", ErrorCode.UnauthorizedException.getErrorCode());

        }
        catch (IllegalArgumentException e){
            log.info("JWT claims string is empty", e);
            request.setAttribute("exception", ErrorCode.UnauthorizedException.getErrorCode());

        }return false;
    }


    // 만료 시간 생성
    private Date generateDateExpiresIn(long time){
        long now = (new Date()).getTime();
        Date dateExpiresIn = new Date(now + time);
        return dateExpiresIn;
    }

    // 인증객체를 이용한 사용자 이메일 추출
    private String authenticationGetEmail(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        return  email;

    }



}