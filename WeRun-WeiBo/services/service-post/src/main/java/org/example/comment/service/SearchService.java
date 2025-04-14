package org.example.comment.service;

import org.example.comment.pojo.vo.PageResult;
import org.example.comment.pojo.vo.PostVO;


public interface SearchService {
    //根据输入的关键词u查询帖子并分页展示
    PageResult<PostVO> searchPosts(String keyword, int page, int pageSize);

    //根据标签查询帖子并分页展示
    PageResult<PostVO> searchPostsByTag(String tagName, int page, int pageSize);

    PostVO searchPostsById(String uuid);

    boolean checkPostsById(String uuid, String authorId);
}
