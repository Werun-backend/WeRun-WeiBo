package org.example.post.Service;

import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.VO.PageResult;
import org.springframework.stereotype.Service;


public interface SearchService {
    //根据输入的关键词u查询帖子并分页展示
    PageResult<PostPO> searchPosts(String keyword, int page, int pageSize);

    //根据标签查询帖子并分页展示
    PageResult<PostPO> searchPostsByTag(String tagName, int page, int pageSize);

}
