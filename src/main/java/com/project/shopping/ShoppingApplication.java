package com.project.shopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy
@SpringBootApplication
public class ShoppingApplication {

    public static void main(String[] args) {

        SpringApplication.run(ShoppingApplication.class, args);
    }


}
