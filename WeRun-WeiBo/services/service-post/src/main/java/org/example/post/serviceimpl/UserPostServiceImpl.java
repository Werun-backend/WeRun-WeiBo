package org.example.post.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.example.post.mapper.UserPostMapper;
import org.example.post.pojo.PO.PostPO;
import org.example.post.service.UserPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
@Slf4j
@Service
public class UserPostServiceImpl implements UserPostService {

    @Autowired
    private UserPostMapper userPostMapper;

    @Override
    public PageResult<PostPO> getUserPosts(String authid, int page, int pageSize) {
        // 记录方法进入日志
        log.info("Entering PostServiceImpl.getUserPosts() with authid: {}, page: {}, pageSize: {}", authid, page, pageSize);

        // 参数校验
        if (authid == null || authid.isEmpty()) {
            log.error("Authid is null or empty");
            throw new IllegalArgumentException("Authid cannot be null or empty");
        }
        if (page <= 0 || pageSize <= 0) {
            log.error("Invalid pagination parameters: page={}, pageSize={}", page, pageSize);
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }

        try {
            // 计算偏移量
            int offset = (page - 1) * pageSize;

            // 查询用户帖子及总数
            List<PostPO> list = userPostMapper.selectUserPosts(authid, offset, pageSize);
            int total = userPostMapper.countUserPosts(authid);

            // 返回分页结果
            return new PageResult<>(total, list, page, pageSize);
        } catch (Exception e) {
            log.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }
    }

//    /**
//     * 计算分页偏移量
//     *
//     * @param page     当前页码
//     * @param pageSize 每页大小
//     * @return 偏移量
//     */
//    private int calculateOffset(int page, int pageSize) {
//        return (page - 1) * pageSize;
//    }
}


