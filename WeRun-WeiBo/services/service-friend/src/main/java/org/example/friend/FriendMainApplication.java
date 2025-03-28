package org.example.friend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
// 开启服务发现
@EnableDiscoveryClient
@EnableOAuth2Sso
public class FriendMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(FriendMainApplication.class, args);
    }
}
