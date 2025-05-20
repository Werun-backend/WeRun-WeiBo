package org.example.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author 32218
 */
@SpringBootApplication
@EnableAsync
public class AuthMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthMainApplication.class, args);
    }
}
