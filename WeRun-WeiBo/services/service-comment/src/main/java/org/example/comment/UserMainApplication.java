package org.example.comment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 这是授权服务器，所有请求会拦截到这里进行重定向到登录接口
 */
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class UserMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserMainApplication.class, args);
    }
}
