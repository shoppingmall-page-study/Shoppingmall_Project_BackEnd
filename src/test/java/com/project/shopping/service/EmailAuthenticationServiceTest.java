package com.project.shopping.service;

import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.CheckAuthCodeRequestDTO;
import com.project.shopping.dto.requestDTO.EmailAuthenticationRequestDTO.SendAuthCodeRequestDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.CheckAuthCodeResponseDTO;
import com.project.shopping.dto.responseDTO.EmailAuthenticationResponseDTO.SendAuthCodeResponseDTO;
import com.project.shopping.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.mail.javamail.JavaMailSender;

import javax.mail.Session;
import javax.mail.internet.MimeMessage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class EmailAuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Spy
    private ValueOperations valueOperations;


    @InjectMocks
    private EmailAuthenticationService emailAuthenticationService;

    @Test
    @DisplayName("## 인증 이메일 전송 서비스 테스트 ##")
    public void sendAuthenticationCode () throws Exception{

        SendAuthCodeRequestDTO sendAuthCodeRequest = SendAuthCodeRequestDTO.builder()
                .email("test@gmail.com")
                .build();

        SendAuthCodeResponseDTO sendAuthCodeResponse = SendAuthCodeResponseDTO.builder()
                .email("test@gmail.com")
                .build();

        MimeMessage mimeMessage = new MimeMessage((Session) null);

        //given
        given(javaMailSender.createMimeMessage()).willReturn(mimeMessage);
        given(userRepository.existsByEmail(any())).willReturn(false);
        willDoNothing().given(javaMailSender).send(any(MimeMessage.class));
        given(redisTemplate.opsForValue()).willReturn(valueOperations);

        //when
        SendAuthCodeResponseDTO result = emailAuthenticationService.sendAuthenticationCode(sendAuthCodeRequest);

        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(sendAuthCodeResponse);

    }

    @Test
    @DisplayName("## 인증코드 일치 확인 서비스 테스트 ##")
    public void checkAuthCode () throws Exception{

        CheckAuthCodeRequestDTO checkAuthCodeRequest = CheckAuthCodeRequestDTO.builder()
                .email("test@gmail.com")
                .authCode("test")
                .build();

        CheckAuthCodeResponseDTO checkAuthCodeResponse = CheckAuthCodeResponseDTO.builder()
                .email("test@gmail.com")
                .build();


        //given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(any())).willReturn("test");

        //when
        CheckAuthCodeResponseDTO result = emailAuthenticationService.checkAuthCode(checkAuthCodeRequest);


        //then
        assertThat(result).usingRecursiveComparison().isEqualTo(checkAuthCodeResponse);
    }

    @Test
    @DisplayName("## 이미 인증된 이메일 주소인지 확인 서비스 테스트 ##")
    public void isEmailAuthenticated () throws Exception{

        String email = "test@gmail.com";

        //given
        given(redisTemplate.opsForValue()).willReturn(valueOperations);
        given(valueOperations.get(any())).willReturn("Authenticated");

        //when
        boolean result = emailAuthenticationService.isEmailAuthenticated(email);

        //then
        assertThat(result).isTrue();
    }

}