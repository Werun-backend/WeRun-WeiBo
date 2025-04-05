package org.example.post.serviceimpl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.post.mapper.PostMapper;
import org.example.post.pojo.PO.PostPO;
import org.example.post.service.PostService;
import org.example.post.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@Slf4j
@Data
public class PostServiceImpl implements PostService {

    @Autowired
    private PostMapper postMapper;
    @Autowired
    private ScheduleService scheduleService;

    public void publishPost(PostPO postPO, List<String> newtags, List<String> selectedTags) {
        log.info("PostServiceImpl.insertPost()");

        // 检查是否为定时发布
        if (postPO.getSchedule() != null && postPO.getSchedule() == 0) {
            // 定时发布的逻辑
            if (postPO.getScheduleTime() == null) {
                log.error("Scheduled time is not set for the post");
                return;
            }
            long executeTime = postPO.getScheduleTime().toInstant().toEpochMilli();
            scheduleService.schedulePost(postPO,newtags,selectedTags,executeTime);}

            else {
                // 非定时发布的逻辑
                // 插入帖子
                postMapper.insertPost(postPO);

                // 查询帖子的 ID
                String postId = postPO.getUuid();

                // 展示所有标签及其Id
                List<String> tags = postMapper.selectTags();


                //查询标签库中某个标签的Id 插入标签
                List<Long> tagIds = selectedTags.stream()
                        .map(tagName -> postMapper.selectTagId(tagName))
                        .collect(Collectors.toList());
                for (String tagName : selectedTags) {
                    Long tagId = postMapper.selectTagId(tagName);
                    if (tagId != null) {
                        tagIds.add(tagId);
                        postMapper.insertPostTag(postId, tagId);
                    }
                }


                // 插入新的标签并获取ID 插入标签
                if (newtags != null && !newtags.isEmpty()) {
                    newtags.forEach(tagName -> {
                        Long tagId = postMapper.selectTagId(tagName);
                        if (tagId == null) {
                            postMapper.insertTag(tagName);
                            tagId = postMapper.selectTagId(tagName);
                            postMapper.insertPostTag(postId, tagId);
                        }
                    });
                }
            }


        }



    /**
     * 插入帖子及其标签关联
     */
//    @Override
//    public void insertPost(PostPO postPo, List<String> newtags, List<String> selectedTags) {
//        log.info("PostServiceImpl.insertPost()");
//        //判断是否为定时帖子
//
//
//            // 插入帖子
//            postMapper.insertPost(postPo);
//
//            // 查询帖子的 ID
//            String postId = postPo.getUuid();
//
//            // 展示所有标签及其Id
//            List<String> tags = postMapper.selectTags();
//
//
//            //查询标签库中某个标签的Id 插入标签
//            List<Long> tagIds = selectedTags.stream()
//                    .map(tagName -> postMapper.selectTagId(tagName))
//                    .collect(Collectors.toList());
//            for (String tagName : selectedTags) {
//                Long tagId = postMapper.selectTagId(tagName);
//                if (tagId != null) {
//                    tagIds.add(tagId);
//                    postMapper.insertPostTag(postId, tagId);
//                }
//            }
//
//
//            // 插入新的标签并获取ID 插入标签
//            if (newtags != null && !newtags.isEmpty()) {
//                newtags.forEach(tagName -> {
//                    Long tagId = postMapper.selectTagId(tagName);
//                    if (tagId == null) {
//                        postMapper.insertTag(tagName);
//                        tagId = postMapper.selectTagId(tagName);
//                        postMapper.insertPostTag(postId, tagId);
//                    }
//                });
//            }
//        }
//


    /**
     * 更新帖子  仅包含删除标签
     */
    @Override
    public void updatePost(PostPO postPo, List<String> deletetags, List<String> newtags, List<String> selectedtags) {
        log.info("PostServiceImpl.updatePost()");
        postMapper.updatePost(postPo);

        // 查询帖子的 ID
        String postId = postPo.getUuid();


//        //查看所改帖子关联的所有标签
 //      List<String> tags = postMapper.selectTagsByPostId(Long.parseLong(postId));

//        //查询deletatags的Id
//        List<String> tags = postMapper.selectTagsByPostId(Long.parseLong(postId));

        // 删除帖子关联的某个标签
        if (deletetags != null && !deletetags.isEmpty()) {
            deletetags.forEach(tagName -> {
                Long tagId = postMapper.selectTagId(tagName);
                if (tagId != null) {
                    postMapper.deletePostTags(Long.parseLong(postId), tagId);
                }
            });
        }
        //查询标签库中某个标签的Id 插入标签
//        List<Long> tagIds = selectedtags.stream()
//                .map(tagName -> postMapper.selectTagId(tagName))
//                .collect(Collectors.toList());
        for (String tagName : selectedtags) {
            Long tagId = postMapper.selectTagId(tagName);
            if (tagId != null) {
//                tagIds.add(tagId);
                postMapper.insertPostTag(postId, tagId);
            }
        }


        // 插入新的标签并获取ID 插入标签
        if (newtags != null && !newtags.isEmpty()) {
            newtags.forEach(tagName -> {
                Long tagId = postMapper.selectTagId(tagName);
                if (tagId == null) {
                    postMapper.insertTag(tagName);
                    tagId = postMapper.selectTagId(tagName);
                    postMapper.insertPostTag(postId, tagId);
                }
            });
        }

    }

    /**
     * 删除帖子
     */
    @Override
    public void deletePost(String uuid) {
        log.info("PostServiceImpl.deletePost()");
        postMapper.deletePost(uuid);
        // 实现删除逻辑
    }

    /**
     * 插入标签并返回标签ID
     *
     * @param tagName 标签名
     * @return 标签ID
     */

}
