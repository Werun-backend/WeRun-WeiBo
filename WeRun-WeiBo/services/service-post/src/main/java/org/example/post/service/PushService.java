package org.example.post.service;

import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;

public interface PushService {
    PageResult<PostVO> pushPosts(int page, int pageSize);
    PageResult<PostVO> pushPostsByCommentCount(int page, int pageSize);
}
