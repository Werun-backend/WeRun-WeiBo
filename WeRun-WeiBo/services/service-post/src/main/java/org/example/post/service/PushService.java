package org.example.post.service;

import org.example.post.pojo.bo.PostBO;
import org.example.post.pojo.vo.PageResult;
import org.example.post.pojo.vo.PostVO;

public interface PushService {
    PageResult<PostVO> pushPosts(int page, int pageSize);
}
