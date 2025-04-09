package org.example.post.Service.impl;

import org.example.common.model.user.UserBO;
import org.example.post.Mapper.PostMapper;
import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.DTO.UpdateDTO;
import org.example.post.Service.PostService;
import org.example.post.Utils.JwtUtils;
import org.example.post.Utils.RedisIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Service
public class PostServiceImpl implements PostService {

    public PostServiceImpl(PostMapper postMapper, StringRedisTemplate stringRedisTemplate, RedisIdWorker redisIdWorker) {
        this.postMapper = postMapper;
        this.stringRedisTemplate = stringRedisTemplate;
        this.redisIdWorker = redisIdWorker;
    }

    private final PostMapper postMapper;
    private final StringRedisTemplate stringRedisTemplate;
    private final RedisIdWorker redisIdWorker;
    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);

    public void publishPost(PostPO postPO) {
        logger.debug("正在进行发布");
        if (postPO.getSchedule() == null) {
            logger.error("选择类别出现问题");
            return;
        }
        // 检查是否为定时发布
        if (postPO.getSchedule() == 0) {
            // 定时发布的逻辑
            logger.debug("现在进行定时发布");
            if (postPO.getScheduleTime() == null) {
                logger.error("帖子发送状态出现问题");
                return;
            }
            try {
                // 确保 getScheduleTime() 返回的是 LocalDateTime 类型
                LocalDateTime scheduleTime = postPO.getScheduleTime();
                logger.info("定时发布时间：{}", scheduleTime);
                logger.debug("定时发布时间正常进行");
                long executeTime = scheduleTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                logger.debug("定时发布时间转换");
                schedulePost(postPO, executeTime + 10);
                logger.debug("完成定时发布服务");
            } catch (Exception e) {
                logger.error("Error while processing scheduled time: {}", e.getMessage(), e);
            }
        } else if (postPO.getSchedule() == 1 || postPO.getSchedule() == 2) {
            logger.debug("开始发布帖子");
            postMapper.insertPost(postPO);
            logger.debug("帖子发布完成");// 查询帖子的 ID
            String postId = postPO.getUuid();
            logger.debug("将标签放到数据库进行管理");
            if (postPO.getTags() != null && !postPO.getTags().isEmpty()) {
                postPO.getTags().forEach(StoreTag(postId));
            }
        }
    }

    /**
     * 更新帖子  仅包含删除标签
     */
    @Override
    public void updatePost(UpdateDTO updateDto) {
        logger.debug("现在进入更新帖子的服务");
        postMapper.updatePost(updateDto);
        logger.debug("将更新的内容存到数据库");
        String postId = updateDto.getUuid();
        if (updateDto.getDeleteTags() != null) {
            logger.debug("删除标签");
            updateDto.getDeleteTags().forEach(tagName -> {
                postMapper.deletePostTags(postId, tagName);
                logger.info("删除标签{}，{}", postId, tagName);
            });
            logger.debug("删除标签完成");
        }
        if (updateDto.getTags() != null && !updateDto.getTags().isEmpty()) {
            updateDto.getTags().forEach(StoreTag(postId));
        }

    }

    private Consumer<String> StoreTag(String postId) {
        return tagName -> {
            if (tagName != null) {
                logger.info("存入标签{}，{}", postId, tagName);
                postMapper.insertPostTag(postId, tagName);
            }
        };
    }

    /**
     * 删除帖子
     */
    @Override
    public void deletePost(String uuid) {
        logger.debug("现在进行删除帖子的操作");
        postMapper.deletePost(uuid);
        logger.debug("删除帖子完成");
        // 实现删除逻辑
    }

    @Override
    public PostPO assemblePO(PostDTO postDTO, String token) {
        String jwt = token.substring(7);
        UserBO user = (UserBO) JwtUtils.parseJWT(jwt).get("user");
        return new PostPO(String.valueOf(redisIdWorker.nextId("post")), user.getUuid(), postDTO.getTitle(), postDTO.getContent(), postDTO.getSchedule(), postDTO.getScheduleTime(), postDTO.getTags());
    }

    private void schedulePost(PostPO postPO, long executeTime) {
        logger.debug("正在进行帖子定时发布工作，现在在用redis缓存数据");
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", postPO.getUuid());
        map.put("authorId", postPO.getAuthorId());
        map.put("title", postPO.getTitle());
        map.put("content", postPO.getContent());
        map.put("scheduleTime", postPO.getScheduleTime());
        map.put("tags", postPO.getTags());
        JwtUtils jwtUtils = new JwtUtils();
        jwtUtils.setEXPIRATION(executeTime);
        String jwt = jwtUtils.generateToken(map);
        stringRedisTemplate.opsForValue().set("post:" + postPO.getUuid(), jwt);
        if (postPO.getTags() != null && !postPO.getTags().isEmpty()) {
            stringRedisTemplate.opsForList().rightPushAll("tags:selected:" + postPO.getUuid(), postPO.getTags());
        }
    }
}