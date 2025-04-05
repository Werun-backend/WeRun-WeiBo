package org.example.post.controller;

import org.example.post.pojo.PO.PostPO;
import org.example.post.service.UserPostService;
import org.example.post.serviceimpl.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weibo/userpost")
public class UserPostController {
    @Autowired
    private UserPostService userPostService;

    @RequestMapping("/getUserPosts")
    public PageResult<PostPO> getUserPosts(String authid, int page, int pageSize) {
        return userPostService.getUserPosts(authid, page, pageSize);
    }

}
