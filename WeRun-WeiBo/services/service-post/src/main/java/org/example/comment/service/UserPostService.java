package org.example.comment.service;

import org.example.comment.pojo.vo.PageResult;
import org.example.comment.pojo.vo.PostVO;

public interface UserPostService {

    PageResult<PostVO> getUserPosts(String authid, int page, int pageSize) ;

}
