package org.example.gateway.config;

import io.jsonwebtoken.Claims;
import org.example.gateway.Utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class GatewayConfig implements GlobalFilter {

    private final StringRedisTemplate stringRedisTemplate;

    public GatewayConfig(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }
    Logger logger = LoggerFactory.getLogger(GatewayConfig.class);
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.debug("现在在网关过滤器");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        logger.info("获取到请求头为:{}", token);
        if (token == null || !token.startsWith("Bearer ")) {
            logger.error("请求头为空或者请求头的开头没有加上Bearer前缀");
            return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized"));
        }
        logger.debug("完成请求头规范校验");
        String jwt = token.substring(7);
        logger.info("成功获取到JWT为:{}",jwt);
        if(Objects.equals(stringRedisTemplate.opsForValue().get("blacklist:jwt:" + jwt), "BLACK")){
            logger.error("请求头被列入了黑名单");
            throw new RuntimeException("Token has been blacklisted");
        }
        logger.debug("黑名单检测完成");
        try {
            logger.debug("尝试解析JWT令牌");
            Claims claims = JwtUtils.parseJWT(jwt);
            logger.debug("解析完成，返回结果:{}",claims);
        return chain.filter(exchange);
    } catch (Exception e) {
            logger.error("出现意料之外的错误");
            throw new RuntimeException(e);
        }
    }
}
