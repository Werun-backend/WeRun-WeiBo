package org.example.comment.service;

import org.example.comment.pojo.vo.PageResult;
import org.example.comment.pojo.vo.PostVO;

public interface PushService {
    PageResult<PostVO> pushPosts(int page, int pageSize);
}
