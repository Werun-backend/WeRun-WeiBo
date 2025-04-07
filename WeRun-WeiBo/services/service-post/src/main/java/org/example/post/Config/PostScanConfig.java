package org.example.post.Config;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.Service.PostService;
import org.example.post.Service.ScheduleService;
import org.example.post.Utils.JwtUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.Set;

@Configuration
@EnableScheduling
public class PostScanConfig {
    private final StringRedisTemplate stringRedisTemplate;
    private final PostService postService;
    private final ScheduleService scheduleService;

    public PostScanConfig(StringRedisTemplate stringRedisTemplate, PostService postService, ScheduleService scheduleService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.postService = postService;
        this.scheduleService = scheduleService;
    }
    @Scheduled(fixedRate = 1000)
    public void scanAndExecuteTask() {
        List<String> tasks = stringRedisTemplate.opsForList().range("post:*", 0, -1);
        if (tasks != null&& !tasks.isEmpty())
            for (String taskId : tasks) {
                if (JwtUtils.parseJWT(taskId).getExpiration().before(new java.util.Date(System.currentTimeMillis()-1000))) {
                    PostDTO thePost = (PostDTO) JwtUtils.parseJWT(taskId).get("ThePost");
                    scheduleService.updatePostSchedule(thePost.getUuid());
                    thePost.setSchedule(2);
                    postService.publishPost(thePost);
                    stringRedisTemplate.opsForZSet().remove("post", taskId);
                }}
    }
}
