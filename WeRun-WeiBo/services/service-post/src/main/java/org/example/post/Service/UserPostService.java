package org.example.post.Service;

import org.example.post.POJO.BO.PostBO;
import org.example.post.POJO.VO.PageResult;

public interface UserPostService {

    PageResult<PostBO> getUserPosts(String authid, int page, int pageSize) ;

}
