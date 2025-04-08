package org.example.post.Config;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.Service.PostService;
import org.example.post.Utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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
                if (JwtUtils.parseJWT(taskId).getExpiration().before(new java.util.Date(System.currentTimeMillis()-500))) {
                    logger.debug("定时结束进入到发布阶段");
                    PostDTO thePost = (PostDTO) JwtUtils.parseJWT(taskId).get("ThePost");
                    logger.info("解析即将过期的令牌为:{}",thePost);
                    thePost.setSchedule(2);
                    logger.debug("改变获取的帖子的帖子状态");
                    postService.publishPost(thePost);
                    logger.debug("发布帖子");
                }
            }
    }
}
