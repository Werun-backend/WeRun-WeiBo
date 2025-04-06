package org.example.post.serviceimpl;

import org.example.post.mapper.PostMapper;
import org.example.post.pojo.PO.PostPO;
import org.example.post.service.PostService;
import org.example.post.service.ScheduleService;
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

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private PostService postService;
    //添加定时任务
    Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    @Override
    public void schedulePost(PostPO postPO,List<String> newtags,List<String> selectedTags,long executeTime) {
        logger.debug("正在进行定时发布工作，现在在用redis缓存数据");
        redisTemplate.opsForZSet().add("post", postPO.getUuid(), executeTime);

        //储存信息
        if (newtags != null && !newtags.isEmpty()) {
            redisTemplate.opsForList().rightPushAll("tags:new:" + postPO.getUuid(), newtags);
        }
        if (selectedTags != null && !selectedTags.isEmpty()) {
            redisTemplate.opsForList().rightPushAll("tags:selected:" + postPO.getUuid(), selectedTags);
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
                PostPO post = postService.getPostById(taskId);
                if (post != null) {
                    List<String> newtags = redisTemplate.opsForList().range("tags:new:" + taskId, 0, -1);
                    List<String> selectedTags = redisTemplate.opsForList().range("tags:selected:" + taskId, 0, -1);

                    // 发布帖子
                    postService.publishPost(post, newtags, selectedTags);

                    // 从 Sorted Set 中移除已执行的任务
                    redisTemplate.opsForZSet().remove("post", taskId);
                }
            }

    }
}
