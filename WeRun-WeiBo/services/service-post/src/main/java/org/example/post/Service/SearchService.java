package org.example.post.Service;

import org.example.post.POJO.BO.PostBO;
import org.example.post.POJO.VO.PageResult;


public interface SearchService {
    //根据输入的关键词u查询帖子并分页展示
    PageResult<PostBO> searchPosts(String keyword, int page, int pageSize);

    //根据标签查询帖子并分页展示
    PageResult<PostBO> searchPostsByTag(String tagName, int page, int pageSize);

}
