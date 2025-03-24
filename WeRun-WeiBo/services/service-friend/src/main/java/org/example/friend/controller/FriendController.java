package org.example.friend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/weibo/friend")
@RestController
public class FriendController {
    @GetMapping("/test")
    public String test(){
        return "success";
    }
}
