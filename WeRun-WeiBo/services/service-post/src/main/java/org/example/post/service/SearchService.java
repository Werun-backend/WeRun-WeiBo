package org.example.post.service;

import org.example.post.pojo.PO.PostPO;
import org.example.post.serviceimpl.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SearchService {
    //根据输入的关键词u查询帖子并分页展示
    PageResult<PostPO> searchPosts(String keyword, int page, int pageSize);

    //根据标签查询帖子并分页展示
    PageResult<PostPO> searchPostsByTag(String tagName, int page, int pageSize);

}
