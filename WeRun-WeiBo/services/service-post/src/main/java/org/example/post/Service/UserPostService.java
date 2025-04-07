package org.example.post.Service;

import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.VO.PageResult;

public interface UserPostService {

    public PageResult<PostPO> getUserPosts(String authid, int page, int pageSize) ;

}
