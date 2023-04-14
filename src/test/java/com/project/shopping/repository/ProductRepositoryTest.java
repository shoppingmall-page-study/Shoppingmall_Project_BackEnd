package com.project.shopping.repository;


import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductRepositoryTest {


//    @Autowired ProductRepository productRepository;
//    @Autowired UserRepository userRepository;
//    @Autowired   PasswordEncoder passwordEncoder;
////
//    @Test
//    public void save(){
//        String passwordd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("productrepo1@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passwordd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .postCode("postCode")
//                .build();
//        userRepository.save(user);
//        Product product = Product.builder()
//                .user(user)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product testProduct = productRepository.save(product);
//
//
//        Assertions.assertThat(product.getuser()).isEqualTo(testProduct.getuser());
//
//    }
//
////
//    @Test
//    public void findById(){
//        String passwordd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("productrepo2@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passwordd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .postCode("tst")
//                .build();
//        userRepository.save(user);
//        Product product1 = Product.builder()
//                .user(user)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product testProduct = productRepository.save(product1);
//
//        Product findById = productRepository.findById(testProduct.getId());
//        Assertions.assertThat(product1.getId()).isEqualTo(findById.getId());
//    }
//
//    @Test
//    public void findByIdAnduser(){
//        String passwordd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("productrepo3@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passwordd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .postCode("test")
//                .build();
//        userRepository.save(user);
//        Product product1 = Product.builder()
//                .user(user)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product testProduct = productRepository.save(product1);
//
//        Product findIdAnduser = productRepository.findByIdAnduser(testProduct.getId(),testProduct.getuser());
//
//        Assertions.assertThat(product1.getuser().getEmail()).isEqualTo(findIdAnduser.getuser().getEmail());
//    }
//
//    @Test
//    public void findAll(){
//        String passwordd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("productrepo4@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passwordd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .postCode("test")
//                .build();
//        userRepository.save(user);
//        Product product1 = Product.builder()
//                .user(user)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .status("active")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//        Product testProduct = productRepository.save(product1);
//        List<Product> findAll = productRepository.findAll();
//        for(Product product : findAll){
//            if(product.getId() == product1.getId()){
//                Assertions.assertThat(product.getId()).isEqualTo(product1.getId());
//            }
//        }
//    }
}
