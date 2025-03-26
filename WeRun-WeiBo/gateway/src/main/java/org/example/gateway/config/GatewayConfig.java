package org.example.gateway.config;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.example.gateway.Utils.JwtUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig implements GlobalFilter {
    private final StringRedisTemplate stringRedisTemplate;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }
        // 验证JWT令牌
        try {
            // 解析JWT令牌，获取用户信息
            Claims claims = JwtUtils.parseJWT(token.substring(7));
            // 如果解析失败，抛出异常
            String id = claims.getId();
            String redisKey = "jwt:" + Integer.parseInt(id);
            String redisValue = stringRedisTemplate.opsForValue().get(redisKey);
            // 如果请求头中的token为空，返回未登录信息
            if(redisValue==null || redisValue.isEmpty()){
                throw new RuntimeException("身份校验失败");
            }
            if (!stringRedisTemplate.hasKey(redisKey) || !redisValue.trim().equals(token.substring(7))) {
                throw new RuntimeException("身份校验失败");
            }
        } catch (Exception e) {
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token"));
        }
        return chain.filter(exchange);
    }
}
