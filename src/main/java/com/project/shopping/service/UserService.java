package com.project.shopping.service;


import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User create(User user){
        System.out.println(user.getEmail());
        if(user == null || user.getEmail().equals("")){
            throw new NoSuchElementException("잘못된 형식의 데이터 입니다. ");
        }
        final String email = user.getEmail();
        //System.out.println(userRepository.existsByEmail(email)+"이메일이 존재하는지?");

        //왜 여기서 안걸리지 ??

/*        if(userRepository.existsByEmail(email)){
            // 이메일이 존재할시
            log.warn("email already exists", email);
            throw new RuntimeException();
        }*/
        return userRepository.save(user);
    }

    public User SaveUser(User user){
        return  userRepository.save(user);
    }

    public User findEmailByUser(String email){
        return userRepository.findByEmail(email);
    }

    //login 인증
//    public User getByCredentials(final String email , final String password, final PasswordEncoder encoder){
//        final User original = userRepository.findByEmail(email);
//        if(original != null && encoder.matches(password, original.getPassword())){
//            return original;
//        }
//        return null;
//    }

    public Boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }

    public Boolean existsByNickname(String nickname){return userRepository.existsByNickname(nickname);}
    public User updateUser(User user){
        return  userRepository.save(user);
    }
}
