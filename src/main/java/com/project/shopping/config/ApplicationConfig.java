package com.project.shopping.config;


import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
public class ApplicationConfig {

    // WebSecurityConfig 에서 사용시 WebSecurityConfig -> CustomOAuth2UserService -> UserService 에서 순환 참조 현상 발생
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE).setFieldMatchingEnabled(true);
        return modelMapper;
    }
}
