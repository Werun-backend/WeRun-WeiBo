package org.example.post.Service.impl;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.PO.PostPO;
import org.example.post.Service.PostService;
import org.example.post.Service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final RedisTemplate<String, String> redisTemplate;
    private final PostService postService;
    private final ScheduleService scheduleService;

    public ScheduleServiceImpl(RedisTemplate<String, String> redisTemplate, PostService postService, ScheduleService scheduleService) {
        this.redisTemplate = redisTemplate;
        this.postService = postService;
        this.scheduleService = scheduleService;
    }
    Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    @Override
    public void schedulePost(PostDTO postDTO, long executeTime) {
        logger.debug("正在进行帖子定时发布工作，现在在用redis缓存数据");
        redisTemplate.opsForZSet().add("post", postDTO.getUuid(), executeTime);
        //储存信息
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            redisTemplate.opsForList().rightPushAll("tags:new:" + postDTO.getUuid(), postDTO.getTags());
        }
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            redisTemplate.opsForList().rightPushAll("tags:selected:" + postDTO.getUuid(), postDTO.getTags());
        }
    }
    @Override
    @Scheduled(fixedRate = 1000)
    public void scanAndExecuteTask() {
        long currentTime = System.currentTimeMillis();

        //获取当前时间需要执行的任务
        Set<String>tasks = redisTemplate.opsForZSet().rangeByScore("post", 0, currentTime);
        if (tasks != null&& !tasks.isEmpty())
            for (String taskId : tasks) {
                // 根据 taskId 获取帖子信息
                PostDTO post = postService.getPostById(taskId);
                if (post != null) {
                    List<String> newtags = redisTemplate.opsForList().range("tags:new:" + taskId, 0, -1);
                    List<String> selectedTags = redisTemplate.opsForList().range("tags:selected:" + taskId, 0, -1);

                    // 发布帖子
                    postService.publishPost(post);

                    //将schedule字段设置为2
                    scheduleService.updatePostSchedule(taskId);


                    // 从 Sorted Set 中移除已执行的任务
                    redisTemplate.opsForZSet().remove("post", taskId);
                }
            }

    }

    @Override
    public void updatePostSchedule(String uuid) {
        scheduleService.updatePostSchedule(uuid);
    }
}
