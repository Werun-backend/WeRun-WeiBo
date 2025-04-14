package org.example.comment.service.impl;

import org.example.comment.mapper.PushPostMapper;
import org.example.comment.pojo.vo.PageResult;
import org.example.comment.pojo.vo.PostVO;
import org.example.comment.service.PushService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushServiceImpl implements PushService {

    private final PushPostMapper pushPostMapper;

    public PushServiceImpl(PushPostMapper pushPostMapper) {
        this.pushPostMapper = pushPostMapper;
    }
    Logger logger = LoggerFactory.getLogger(PushServiceImpl.class);
    //查询所有帖子并分页展示
    @Override
    public PageResult<PostVO> pushPosts(int page, int pageSize) {
        logger.debug("正在进行推送服务");
        List<PostVO> allPosts = pushPostMapper.selectAllPosts();
        logger.debug("推送数据获取完成");
        logger.debug("开始分页");
        int total = allPosts.size();
        int offset = (page - 1) * pageSize;
        int end = Math.min(offset + pageSize, total);
        List<PostVO> pagePosts = allPosts.subList(offset, end);
        logger.debug("返回分页后的数据");
        return new PageResult<>(total, pagePosts, page, pageSize);
    }

}
