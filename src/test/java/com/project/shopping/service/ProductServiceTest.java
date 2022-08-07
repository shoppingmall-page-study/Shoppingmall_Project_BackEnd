package com.project.shopping.service;

import com.project.shopping.model.Product;
import com.project.shopping.model.User;
import com.project.shopping.repository.UserRepository;
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
public class ProductServiceTest {
    @Autowired
    private ProductService productService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void Create(){
        String passwordd ="asdfasfdasdf";
        User user = User.builder()
                .email("user@example.com")
                .username("user")
                .password(passwordEncoder.encode((passwordd)))
                .address("address")
                .age(11)
                .nickname("nickname")
                .phoneNumber("01000000000")
                .build();
        userRepository.save(user);
        Product product = Product.builder()
                .userId(user)
                .title("가나다")
                .name("치킨")
                .content("clzls")
                .price(10000)
                .total(1000)
                .imgUrl("asdfasdf")
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Product createProduct = productService.create(product);

        Assertions.assertThat(product.getId()).isEqualTo(createProduct.getId());
    }

//    @Test
//    public void deleteProdcut(){
//        String passwordd ="asdfasfdasdf";
//        User user = User.builder()
//                .email("user@example.com")
//                .username("user")
//                .password(passwordEncoder.encode((passwordd)))
//                .address("address")
//                .age(11)
//                .nickname("nickname")
//                .phoneNumber("01000000000")
//                .build();
//        userRepository.save(user);
//        Product product = Product.builder()
//                .userId(user)
//                .title("가나다")
//                .name("치킨")
//                .content("clzls")
//                .price(10000)
//                .total(1000)
//                .imgUrl("asdfasdf")
//                .createDate(Timestamp.valueOf(LocalDateTime.now()))
//                .build();
//
//        Product createProduct = productService.create(product); // 상품 생성후
//
//        productService.deleteProduct(createProduct);
//
//        Product findProduct = productService.findproductid(product.getId());
//        assertEquals(findProduct.getUserId(),null);
//
//
//
//
//    }

    @Test
    public void findall(){
        String passwordd ="asdfasfdasdf";
        User user = User.builder()
                .email("user@example.com")
                .username("user")
                .password(passwordEncoder.encode((passwordd)))
                .address("address")
                .age(11)
                .nickname("nickname")
                .phoneNumber("01000000000")
                .build();
        userRepository.save(user);
        Product product = Product.builder()
                .userId(user)
                .title("가나다")
                .name("치킨")
                .content("clzls")
                .price(10000)
                .total(1000)
                .imgUrl("asdfasdf")
                .createDate(Timestamp.valueOf(LocalDateTime.now()))
                .build();

        Product createProduct = productService.create(product); // 상품 생성후

        List<Product> findall = productService.findall();
        Product findproductId = productService.findproductid(createProduct.getId());
        Assertions.assertThat(findall.get(0).getId()).isEqualTo(findproductId.getId());

    }
}
