package org.example.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/weibo/user")
@RestController
public class UserController {

    @GetMapping("/test")
    public String test(){
        return "success";
    }
}
