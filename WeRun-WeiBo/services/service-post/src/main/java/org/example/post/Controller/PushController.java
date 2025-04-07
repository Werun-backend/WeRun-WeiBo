package org.example.post.Controller;

import org.example.post.POJO.PO.PostPO;
import org.example.post.Service.PushService;
import org.example.post.POJO.VO.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/weibo/push")
@RestController
public class PushController {

    private final PushService pushService;

    public PushController(PushService pushService) {
        this.pushService = pushService;
    }
    @RequestMapping(value = "/pushPosts")
    public PageResult<PostPO> pushPosts(int page, int pageSize) {
        return pushService.pushPosts(page, pageSize);
    }
}
