package org.example.post.Controller;

import org.example.post.POJO.BO.PostBO;
import org.example.post.Service.PushService;
import org.example.post.POJO.VO.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/post/push")
@RestController
public class PushController {

    private final PushService pushService;

    public PushController(PushService pushService) {
        this.pushService = pushService;
    }
    @RequestMapping(value = "/pushPosts")
    public PageResult<PostBO> pushPosts(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer pageSize) {
        return pushService.pushPosts(page, pageSize);
    }
}
