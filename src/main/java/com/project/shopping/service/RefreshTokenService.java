package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.model.User;
import com.project.shopping.repository.RedisRepository;
import com.project.shopping.repository.UserRepository;
import com.project.shopping.security.Token;
import com.project.shopping.security.Tokenprovider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {
    private  final RedisRepository redisRepository;
    private  final Tokenprovider tokenprovider;
    private  final UserRepository userRepository;

    public Token reissuanceRefreshToken(HttpServletRequest request, String refreshToken){
        String accessToken = resolveToken( request);
        String userEmail = tokenprovider.TokenInfo(accessToken);


        //redis를 에 저장된 refresh 토큰 찾기
        Token token = redisRepository.getValues(userEmail);
        String redisSavedRefreshToken = token.getRefreshToken();

        log.info("redis 현재 담긴 토큰", redisSavedRefreshToken);
        log.info("현재 쿠키에 있는 토큰", refreshToken);



        // redis 토큰 확인하기
        if(redisSavedRefreshToken.equals(refreshToken)){

            User user = userRepository.findByEmail(userEmail)
                    .orElseThrow(()->new CustomException("User not found", ErrorCode.NotFoundUserException));


            // 인증 객체 생성 후
            PrincipalDetails principalDetails = new PrincipalDetails(user);
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails,null,principalDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);


            //토큰 재발급
            Token reissuanceToekn = tokenprovider.generateToken(authentication);



            return reissuanceToekn;
        }else{
           throw new CustomException("Not Fount RefrshToken", ErrorCode.NotFoundRefrshTokenException);
        }

    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            return bearerToken.substring(7);
        }
        return  null;
    }
}
