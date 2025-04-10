package com.example.common;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.example.auth", 
        "com.example.common", 
        "com.example.user_account",
        "com.example.emotion_analysis", 
        "com.example.emotion_recommend"
})
@EntityScan(basePackages = "com.example.user_account.entity")
@EnableJpaRepositories(basePackages = "com.example.user_account.repository")
public class UserregistrationApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserregistrationApplication.class, args);
    }
}
