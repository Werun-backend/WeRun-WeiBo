package org.example.post.Controller;

import org.example.post.POJO.BO.PostBO;
import org.example.post.Service.UserPostService;
import org.example.post.POJO.VO.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post/userpost")
public class UserPostController {

    private final UserPostService userPostService;

    public UserPostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @RequestMapping("/getUserPosts")
    public PageResult<PostBO> getUserPosts(String authid, int page, int pageSize) {
        return userPostService.getUserPosts(authid, page, pageSize);
    }

}
