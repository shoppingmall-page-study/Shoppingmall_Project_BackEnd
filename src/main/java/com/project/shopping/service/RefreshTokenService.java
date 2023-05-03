package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import com.project.shopping.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private  final  RedisTemplate<String, Object> redisTemplate;


    private  final TokenProvider tokenProvider;
    private  final UserRepository userRepository;


    public HttpHeaders reIssuanceRefreshToken(String refreshToken){

        // refreshToken  유무 redis 검증

        // refreshToken 으로 유저 정보 추출
        String userEmail = tokenProvider.TokenInfo(refreshToken);
        log.info("userEmail" + userEmail);
        // 없을시 NotFoundRefreshToken
        String redisSavedRefreshToken = getStringValue(userEmail+"jwtToken");

        log.info("redis 현재 담긴 토큰"+ redisSavedRefreshToken);
        log.info("현재 쿠키에 있는 토큰"+ refreshToken);



        // redis 토큰 확인하기
        if(redisSavedRefreshToken.equals(refreshToken)){

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(()->new CustomException(ErrorCode.NotFoundUserException));


            // 인증 객체 생성 후
            Authentication authentication = generateAuthentication(user);

            //accessToken 재발급
            String reIssuanceAccessToken = tokenProvider.generateAccessToken(authentication);
            // header 생성
            return generateHeader(reIssuanceAccessToken);
        }else{
           throw new CustomException(ErrorCode.NotFoundRefrshTokenException);
        }

    }



    // 헤더에 재발급된 토큰을 저장하여 생성
    private HttpHeaders generateHeader(String reIssuanceAccessToken){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization","Bearer "+reIssuanceAccessToken);

        return  headers;
    }

    // 인증 객체 생성
    private  Authentication generateAuthentication(User user){
        PrincipalDetails principalDetails = new PrincipalDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null,principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return authentication;

    }

    public void deleteRefreshToken(HttpServletResponse response){
        Cookie cookie = new Cookie("refreshToken","null");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

    public String getStringValue(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        String value = (String)valueOperations.get(key);

        if(value == null)
            throw new CustomException(ErrorCode.NotFoundValueException);

        return value;
    }



}
