package org.example.post.Service.impl;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.Service.ScheduleService;
import org.example.post.Utils.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@EnableScheduling
public class ScheduleServiceImpl implements ScheduleService {

    private final StringRedisTemplate stringRedisTemplate;
    private final ScheduleService scheduleService;
    public ScheduleServiceImpl(StringRedisTemplate stringRedisTemplate, ScheduleService scheduleService) {
        this.stringRedisTemplate = stringRedisTemplate;

        this.scheduleService = scheduleService;
    }
    Logger logger = LoggerFactory.getLogger(ScheduleServiceImpl.class);
    @Override
    public void schedulePost(PostDTO postDTO, long executeTime) {
        logger.debug("正在进行帖子定时发布工作，现在在用redis缓存数据");
        Map<String, Object> map = new HashMap<>();
        map.put("ThePost", postDTO);
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setEXPIRATION(executeTime);
        String jwt = jwtUtils.generateToken(map);
        stringRedisTemplate.opsForValue().set("post:"+postDTO.getUuid(),jwt);
        if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
            stringRedisTemplate.opsForList().rightPushAll("tags:selected:" + postDTO.getUuid(), postDTO.getTags());
        }
    }

    @Override
    public void updatePostSchedule(String uuid) {
        scheduleService.updatePostSchedule(uuid);
    }
}
