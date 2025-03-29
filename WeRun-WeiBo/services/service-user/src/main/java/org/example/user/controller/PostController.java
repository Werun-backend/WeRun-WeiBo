package org.example.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("weibo/post")
@RestController
public class PostController {
    @GetMapping("/test")
    public String test(){
        return "success";
    }
}
