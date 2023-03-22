package com.project.shopping.service;

import com.project.shopping.Error.CustomException;
import com.project.shopping.Error.ErrorCode;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.CheckAuthCodeRequestDTO;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.SendAuthCodeRequestDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.CheckAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.SendAuthCodeResponseDTO;
import com.project.shopping.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.Duration;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailAuthenticationService {

    private final UserRepository userRepository;

    private final JavaMailSender javaMailSender;

    private  final RedisTemplate<String, Object> redisTemplate;
    public String createAuthCode(){
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for(int i = 0; i < 8; i++){
            int index = random.nextInt(3);

            switch(index){
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char)(random.nextInt(26) + 65));
                    break;
                case 2:
                    key.append((char)(random.nextInt(26) + 48));
                    break;
            }
        }
        return key.toString();
    }


    // 메일 내용 작성
    public MimeMessage createMessage(String receiver, String authCode) throws MessagingException, UnsupportedEncodingException {
		log.info("보내는 대상 : " + receiver);
		log.info("인증 번호 : " + authCode);

        MimeMessage message = javaMailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, receiver);// 보내는 대상
        message.setSubject("회원가입 이메일 인증");// 제목

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요</h1>";
        msgg += "<h1> 쇼핑몰 사이트 입니다</h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += authCode + "</strong><div><br/> "; // 메일에 인증번호 넣기
        msgg += "</div>";
        message.setText(msgg, "utf-8", "html");// 내용, charset 타입, subtype
        // 보내는 사람의 이메일 주소, 보내는 사람 이름
        message.setFrom(new InternetAddress("typoon0820@naver.com", "Shopping_Admin"));// 보내는 사람

        return message;
    }

    public SendAuthCodeResponseDTO sendAuthenticationCode(SendAuthCodeRequestDTO sendAuthCodeRequestDTO) throws MessagingException, UnsupportedEncodingException {
        String userEmail = sendAuthCodeRequestDTO.getEmail();

        if(userRepository.existsByEmail(userEmail))
            throw new CustomException(ErrorCode.DuplicatedEmilException);

        //코드 생성
        String authCode = createAuthCode();

        //레디스 key-value 이메일-코드 형식으로 저장
        saveEmailAuthCode(authCode, userEmail);
        MimeMessage emailForm = createMessage(userEmail, authCode);

        //메일전송
        javaMailSender.send(emailForm);

        return new SendAuthCodeResponseDTO().builder().email(userEmail).build();
    }


    //인증 코드 저장 유효시간 5분 설정
    public void saveEmailAuthCode(String authCode, String email){
        setValueWithExpire( email + "AuthCode", authCode,Duration.ofMinutes(5));
    }

    //인증 되었는지 여부 저장 유효시간 1일 설정
    public void saveIsEmailAuthenticated(String email){
        setValueWithExpire( email + "AuthCode", "Authenticated", Duration.ofDays(1));
    }

    public CheckAuthCodeResponseDTO checkAuthCode(CheckAuthCodeRequestDTO checkAuthCodeRequestDTO){
        String authCode = checkAuthCodeRequestDTO.getAuthCode();
        String userEmail = checkAuthCodeRequestDTO.getEmail();
        String authCodeFromRedis = isValueExist(userEmail+"AuthCode") ? getStringValue(userEmail+"AuthCode") : null;

        if(authCode.equals(authCodeFromRedis)) {
            log.info("인증 코드 값 일치");
            saveIsEmailAuthenticated(userEmail);
        }else{
            throw new CustomException( ErrorCode.BadAuthenticationCodeException);
        }
        return new CheckAuthCodeResponseDTO().builder().email(userEmail).build();
    }


    public boolean isEmailAuthenticated(String email){

        String authCodeFromRedis = isValueExist(email+"AuthCode") ? getStringValue(email+"AuthCode") : "null";

        if(authCodeFromRedis.equals("Authenticated")) {
            return true;
        }
        return false;
    }


    public void setValueWithExpire(String key, Object value, Duration ExpiresIn){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, ExpiresIn);
    }


    public boolean isValueExist(Object key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        Object value = valueOperations.get(key);

        if(value == null)
            return false;

        return true;
    }


    public String getStringValue(String key){
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();

        String value = (String)valueOperations.get(key);

        if(value == null)
            throw new CustomException(ErrorCode.NotFoundValueException);

        return value;
    }


}