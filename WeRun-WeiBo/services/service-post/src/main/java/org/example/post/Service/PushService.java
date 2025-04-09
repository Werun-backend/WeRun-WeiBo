package org.example.post.Service;

import org.example.post.POJO.BO.PostBO;
import org.example.post.POJO.VO.PageResult;

public interface PushService {
    PageResult<PostBO> pushPosts(int page, int pageSize);
}
