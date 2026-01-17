package org.example.post.config;

import io.jsonwebtoken.Claims;
import org.example.common.model.util.JwtUtils;
import org.example.post.pojo.po.PostPO;
import org.example.post.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
public class PostScanConfig {
    private final StringRedisTemplate stringRedisTemplate;
    private final PostService postService;
    Logger logger = LoggerFactory.getLogger(PostScanConfig.class);
    public PostScanConfig(StringRedisTemplate stringRedisTemplate, PostService postService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.postService = postService;
    }
    @Scheduled(fixedRate = 1000)
    public void scanAndExecuteTask() throws InterruptedException {
        Set<String> tasks = stringRedisTemplate.keys("post:*");

        if (!tasks.isEmpty()){

            for (String taskId : tasks) {
                String jwt = stringRedisTemplate.opsForValue().get(taskId);

                Claims claims = JwtUtils.parseJWT(jwt);
                Long StringExp = (Long) claims.get("exp");
                Date exp = new Date(StringExp);
                if (exp.after(new Date(System.currentTimeMillis()))) {
                    logger.info("定时结束进入到发布阶段");
                    logger.info("解析即将过期的令牌为:{}",claims);
                    logger.info("改变获取的帖子的帖子状态");
                    PostPO thePost = new PostPO((String) claims.get("uuid"),
                            (String) claims.get("authorId"),
                            (String) claims.get("title"),
                            (String) claims.get("content"),
                            2,
                            null,
                            (List<String>) claims.get("tags"));
                    postService.publishPost(thePost);
                    logger.info("发布帖子");
                    stringRedisTemplate.delete(taskId);
                }
            }
        }
    }
}
