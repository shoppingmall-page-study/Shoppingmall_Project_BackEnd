package com.project.shopping.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.shopping.auth.PrincipalDetails;
import com.project.shopping.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends JsonIdPwAuthenticationFilter{
    private  final AuthenticationManager authenticationManager;
    private  final TokenProvider tokenProvider;

    private  final RedisTemplate<String, Object> redisTemplate;
    ObjectMapper objectMapper = new ObjectMapper();



    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        try{
            String accessToken = tokenProvider.generateAccessToken(authentication); // accessToken 생성
            String refreshToken = tokenProvider.generateRefreshToken(authentication);// refreshToken 생성

            // 인증객체를 통한 이메일 생성
            String email = useAuthenticationGetEmail(authentication);

            setValue(email+"jwtToken",refreshToken);
            // 쿠키에 refreshToken 저장
            setCookieRefreshToken(response,refreshToken);

            // responseHeader 통한 전송
            sendResponseHeaderAccessToken(response,accessToken);




        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("로그인 시도중");
        try{

            User user = objectMapper.readValue(request.getInputStream(),User.class);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getEmail(),user.getPassword());
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return authentication;
        } catch (IOException e) {
            return null;
        }

    }


    // redis 저장
    public void setValue(String key, Object value){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value);
    }


    // 인증객체를 통한  Email 생성
    private  String useAuthenticationGetEmail(Authentication authentication){
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        String email = principalDetails.getUser().getEmail();
        return  email;

    }

    // responseHeader AccessToken 보내기
    private   void sendResponseHeaderAccessToken(HttpServletResponse response,String accessToken) throws IOException {
        // accessToken header 전송
        response.setContentType("application/json");
        response.addHeader("Authorization","Bearer "+accessToken); // header에 accesstoken 추가

        Map<String, Object> message = new HashMap<>();
        message.put("msg", "login success");
        ResponseEntity.ok().body(message);
        String result = objectMapper.writeValueAsString(message);
        response.getWriter().write(result);

    }

    private  void setCookieRefreshToken( HttpServletResponse response, String refreshToken){
        Cookie cookie =  new Cookie("refreshToken",refreshToken);
        cookie.setPath("/");
        response.addCookie(cookie);

    }

}
