package org.example.post.Service;

import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.VO.PageResult;

public interface PushService {
    PageResult<PostPO> pushPosts(int page, int pageSize);
}
