package org.example.post.controller;

import org.example.post.pojo.PO.PostPO;
import org.example.post.service.PushService;
import org.example.post.serviceimpl.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "weibo/push")
@RestController
public class PushController {
    @Autowired
    private PushService pushService;
    @RequestMapping(value = "pushPosts")
    public PageResult<PostPO> pushPosts(int page, int pageSize) {
        return pushService.pushPosts(page, pageSize);
    }
}
