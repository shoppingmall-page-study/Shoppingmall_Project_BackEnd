package com.project.shopping.service;


import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User create(User user){
        if(user == null && user.getEmail()==null){
            throw new RuntimeException("Invalid arguments");
        }
        final String email = user.getEmail();
        if(userRepository.existsByEmail(email)){
            // 이메일이 존재할시
            log.warn("emali already exists", email);
        }
        return userRepository.save(user);
    }

    //login 인증
    public User getByCredentials(final String email , final String password, final PasswordEncoder encoder){
        final User original = userRepository.findByEmail(email);
        if(original != null && encoder.matches(password, original.getPassword())){
            return original;
        }
        return null;
    }
}
