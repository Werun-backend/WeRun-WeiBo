package org.example.post.service;

import org.example.post.mapper.UserPostMapper;
import org.example.post.pojo.PO.PostPO;
import org.example.post.serviceimpl.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface UserPostService {

    public PageResult<PostPO> getUserPosts(String authid, int page, int pageSize) ;

}
