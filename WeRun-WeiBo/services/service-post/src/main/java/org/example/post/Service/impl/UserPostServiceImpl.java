package org.example.post.Service.impl;

import org.example.post.Mapper.UserPostMapper;
import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.VO.PageResult;
import org.example.post.Service.UserPostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserPostServiceImpl implements UserPostService {

    private final UserPostMapper userPostMapper;

    public UserPostServiceImpl(UserPostMapper userPostMapper) {
        this.userPostMapper = userPostMapper;
    }
    Logger logger = LoggerFactory.getLogger(UserPostServiceImpl.class);
    @Override
    public PageResult<PostPO> getUserPosts(String authid, int page, int pageSize) {
        // 记录方法进入日志
        logger.info("进入按照用户名称查询帖子的操作,用户的ID: {}, page: {}, pageSize: {}", authid, page, pageSize);
        // 参数校验
        if (authid == null || authid.isEmpty()) {
            logger.error("该用户不存在");
            throw new IllegalArgumentException("Authid cannot be null or empty");
        }
        if (page <= 0 || pageSize <= 0) {
            logger.error("Invalid pagination parameters: page={}, pageSize={}", page, pageSize);
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }

        try {
            // 计算偏移量
            int offset = (page - 1) * pageSize;
            // 查询用户帖子及总数
            List<PostPO> list = userPostMapper.selectUserPosts(authid, offset, pageSize);
            logger.info("查询到帖子:{}",list);
            int total = userPostMapper.countUserPosts(authid);
            // 返回分页结果
            return new PageResult<>(total, list, page, pageSize);
        } catch (Exception e) {
            logger.error("Error occurred while fetching user posts", e);
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


