package org.example.comment.controller;

import org.example.common.model.global.BaseResult;
import org.example.comment.service.PushService;
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
    public BaseResult<Object> pushPosts(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer pageSize) {
        return BaseResult.success(pushService.pushPosts(page, pageSize));
    }
}
