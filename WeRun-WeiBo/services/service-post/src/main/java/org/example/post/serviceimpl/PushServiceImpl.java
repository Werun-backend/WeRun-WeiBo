package org.example.post.serviceimpl;

import org.example.post.mapper.PushPostMapper;
import org.example.post.pojo.PO.PostPO;
import org.example.post.service.PushService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PushServiceImpl implements PushService {
    private  PushPostMapper pushPostMapper;
   //查询所有帖子并分页展示
    @Override
    public PageResult<PostPO> pushPosts(int page, int pageSize) {
        List<PostPO> allPosts = pushPostMapper.selectAllPosts();
        int total = allPosts.size();
        int offset = (page - 1) * pageSize;
        int end = Math.min(offset + pageSize, total);
        List<PostPO> pagePosts = allPosts.subList(offset, end);
        return new PageResult<>(total, pagePosts, page, pageSize);
    }

}
