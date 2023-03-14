package com.project.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private  final long MAX_AGE_SECS = 3600;

    // WebSecurityConfig 에서 사용시 WebSecurityConfig -> CustomOAuth2UserService -> UserService 에서 순환 참조 현상 발생
    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://222.118.103.229:3000")
                .allowedOrigins("http://hannam.shop")
                .allowedOrigins("https://hannam.shop")
                .allowedOrigins("http://222.118.103.229:3001")
                .allowedOrigins("http://222.118.103.229")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("Access-Control-Allow-origin","*")
                .allowCredentials(true)
                .exposedHeaders("Authorization","*")
                .maxAge(MAX_AGE_SECS)
        ;
    }
}
