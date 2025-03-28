package org.example.friend.controller;

import org.example.common.model.global.BaseResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/weibo/friend")
@RestController
public class FriendController {
    @GetMapping("/test")
    public BaseResult test(){
        return BaseResult.success("测试成功");
    }
}
