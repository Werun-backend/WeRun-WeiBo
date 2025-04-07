package org.example.post.Service.impl;

import org.example.post.Mapper.PostMapper;
import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.DTO.UpdateDTO;
import org.example.post.Service.PostService;
import org.example.post.Service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class PostServiceImpl implements PostService {

    public PostServiceImpl(PostMapper postMapper, ScheduleService scheduleService) {
        this.postMapper = postMapper;
        this.scheduleService = scheduleService;
    }
    private final PostMapper postMapper;
    private final ScheduleService scheduleService;
    Logger logger = LoggerFactory.getLogger(PostServiceImpl.class);
    public void publishPost(PostDTO postDTO) {
        logger.debug("正在进行发布");
        if (postDTO.getSchedule()== null) {
            logger.error("选择类别出现问题");
            return;
        }
        // 检查是否为定时发布
        if (postDTO.getSchedule() == 0) {
            // 定时发布的逻辑
            logger.debug("现在进行定时发布");
            if (postDTO.getScheduleTime() == null) {
                logger.error("帖子发送状态出现问题");
                return;
            }
            try {
                // 确保 getScheduleTime() 返回的是 LocalDateTime 类型
                LocalDateTime scheduleTime = postDTO.getScheduleTime();
                logger.info("定时发布时间：{}", scheduleTime);
                logger.debug("定时发布时间正常进行");
                long executeTime = scheduleTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                logger.debug("定时发布时间转换");
                scheduleService.schedulePost(postDTO, executeTime+10);
                logger.debug("完成定时发布服务");
            } catch (Exception e) {
                logger.error("Error while processing scheduled time: {}", e.getMessage(), e);
            }
        } else if (postDTO.getSchedule() == 1|| postDTO.getSchedule() == 2){
            logger.debug("开始发布帖子");
            postMapper.insertPost(postDTO);
            logger.debug("帖子发布完成");// 查询帖子的 ID
            String postId = postDTO.getUuid();
            logger.debug("将标签放到数据库进行管理");
            if (postDTO.getTags() != null && !postDTO.getTags().isEmpty()) {
                postDTO.getTags().forEach(tagName -> {
                    if (tagName != null) {
                        logger.info("存入标签{}，{}",postId,tagName);
                        postMapper.insertPostTag(postId, tagName);
                    }
                });
            }
            }
        }
    /**
     * 更新帖子  仅包含删除标签
     */
    @Override
    public void updatePost(UpdateDTO updateDto) {
        logger.info("PostServiceImpl.updatePost()");
        postMapper.updatePost(updateDto);

        // 查询帖子的 ID
        String postId = updateDto.getUuid();


//        //查看所改帖子关联的所有标签
 //      List<String> tags = postMapper.selectTagsByPostId(Long.parseLong(postId));

//        //查询deletatags的Id
//        List<String> tags = postMapper.selectTagsByPostId(Long.parseLong(postId));

        // 删除帖子关联的某个标签
        if (updateDto.getDeletetags() != null)
            updateDto.getDeletetags().forEach(tagName -> {

                    postMapper.deletePostTags(postId, tagName);

            });

        if (updateDto.getTags() != null && !updateDto.getTags().isEmpty()) {
            updateDto.getTags().forEach(tagName  -> {
                if (tagName == null) {
                    postMapper.insertPostTag(postId, tagName);
                }
            });
        }

    }

    /**
     * 删除帖子
     */
    @Override
    public void deletePost(String uuid) {
        logger.info("PostServiceImpl.deletePost()");
        postMapper.deletePost(uuid);
        // 实现删除逻辑
    }

    @Override
    public PostDTO  getPostById(String taskId) {
        return postMapper.selectPostById(taskId);
    }

    /**
     * 插入标签并返回标签ID
     *
     * @param tagName 标签名
     * @return 标签ID
     */

}
