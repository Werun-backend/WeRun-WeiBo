package org.example.post;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

@SpringBootApplication
// 开启服务发现
@EnableDiscoveryClient
@EnableOAuth2Sso
public class PostMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(PostMainApplication.class, args);
    }
}
