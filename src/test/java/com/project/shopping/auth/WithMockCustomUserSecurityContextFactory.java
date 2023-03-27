package com.project.shopping.auth;

import com.project.shopping.model.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        System.out.println(customUser.user());
        User user = User.builder()
                .email(customUser.user())
                .password("password")
                .username("test")
                .address("test")
                .postCode("test")
                .nickname("test")
                .age(20)
                .phoneNumber("010-0000-0000")
                .roles(customUser.roles())
                .build();

        PrincipalDetails principalDetails = new PrincipalDetails(user);

        Authentication auth =
                new UsernamePasswordAuthenticationToken(principalDetails,"password", principalDetails.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}