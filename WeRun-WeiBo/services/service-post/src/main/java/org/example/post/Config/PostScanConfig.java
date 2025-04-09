package org.example.post.Config;

import io.jsonwebtoken.Claims;
import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.PO.PostPO;
import org.example.post.Service.PostService;
import org.example.post.Utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

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
    public void scanAndExecuteTask() {
//        logger.info("正在运行探测");
        List<String> tasks = stringRedisTemplate.opsForList().range("post:*", 0, -1);
        if (tasks != null&& !tasks.isEmpty())
            for (String taskId : tasks) {
                Claims claims = JwtUtils.parseJWT(taskId);
                if (claims.getExpiration().before(new java.util.Date(System.currentTimeMillis()-500))) {
                    logger.debug("定时结束进入到发布阶段");
                    logger.info("解析即将过期的令牌为:{}",claims);
                    logger.debug("改变获取的帖子的帖子状态");
                    PostPO thePost = new PostPO((String) claims.get("uuid"),
                            (String) claims.get("authorId"),
                            (String) claims.get("title"),
                            (String) claims.get("content"),
                            2,
                            (LocalDateTime) claims.get("scheduleTime"),
                            (List<String>) claims.get("tags"));
                    postService.publishPost(thePost);
                    logger.debug("发布帖子");
                }
            }
    }
}
