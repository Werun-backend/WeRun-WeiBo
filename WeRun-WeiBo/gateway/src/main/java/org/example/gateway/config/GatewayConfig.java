package org.example.gateway.config;

import io.jsonwebtoken.Claims;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.gateway.Utils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig implements GlobalFilter {
    @Resource
    private final StringRedisTemplate stringRedisTemplate;

    // 从配置中心获取认证服务器地址
    private static final String AUTH_SERVER_URL = "http://localhost:9010/auth/Oss";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 从请求头中获取JWT令牌
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        // 如果JWT令牌为空，返回未登录信息
        if (token == null || !token.startsWith("Bearer ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }
        // 验证JWT令牌
        try {
            // 解析JWT令牌，获取用户信息
            Claims claims = JwtUtils.parseJWT(token.substring(7));
            // 如果解析失败，抛出异常
            int id = claims.get("id", Integer.class);
            //检查是否在黑名单中
            if (JwtUtils.isTokenBlacklisted(token))
            {
                throw new RuntimeException("Token has been blacklisted");
            }
            // 检查Redis中是否存在该用户的token
            String redisKey = "jwt:" + id;
            String redisValue = stringRedisTemplate.opsForValue().get(redisKey);
            // 如果请求头中的token为空，返回未登录信息
            if(redisValue==null || redisValue.isEmpty()){
                return Redirect(request, response);
            }
            // 如果Redis中不存在该用户的token或匹配不成功，返回未登录信息
            if (!stringRedisTemplate.hasKey(redisKey) || !redisValue.trim().equals(token.substring(7))) {
                return Redirect(request, response);
            }
        } catch (Exception e) {
            return Redirect(request, response);
        }
        return chain.filter(exchange);
    }

    private Mono<Void> Redirect(ServerHttpRequest request, ServerHttpResponse response) {
        // 构造重定向URL
        String redirectUrl = buildRedirectUrl(request);
        // 设置重定向响应
        response.setStatusCode(HttpStatus.FOUND);
        response.getHeaders().setLocation(URI.create(redirectUrl));
        return response.setComplete();
    }

    private String buildRedirectUrl(ServerHttpRequest request) {
        try {
            String originalUrl = request.getURI().toString();
            String encodedUrl = URLEncoder.encode(originalUrl, StandardCharsets.UTF_8);
            return String.format("%s?redirect_uri=%s", AUTH_SERVER_URL, encodedUrl);
        } catch (Exception e) {
            throw new RuntimeException("Failed to build redirect URL", e);
        }
    }

}
