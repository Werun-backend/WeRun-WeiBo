package org.example.post.service;

import org.example.post.pojo.PO.PostPO;
import org.example.post.serviceimpl.PageResult;

public interface PushService {
    PageResult<PostPO> pushPosts(int page, int pageSize);
}
