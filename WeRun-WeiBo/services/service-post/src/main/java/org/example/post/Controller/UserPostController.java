package org.example.post.Controller;

import org.example.post.POJO.PO.PostPO;
import org.example.post.Service.UserPostService;
import org.example.post.POJO.VO.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("weibo/userpost")
public class UserPostController {

    private final UserPostService userPostService;

    public UserPostController(UserPostService userPostService) {
        this.userPostService = userPostService;
    }

    @RequestMapping("/getUserPosts")
    public PageResult<PostPO> getUserPosts(String authid, int page, int pageSize) {
        return userPostService.getUserPosts(authid, page, pageSize);
    }

}
