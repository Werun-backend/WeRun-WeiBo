package org.example.post.controller;

import org.example.common.model.global.BaseResult;
import org.example.post.pojo.dto.PostDTO;
import org.example.post.pojo.po.PostPO;
import org.example.post.pojo.dto.UpdateDTO;
import org.example.post.service.PostService;
import org.example.common.model.util.ThreadContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequestMapping("/post/editPost")
@RestController
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    Logger logger = LoggerFactory.getLogger(PostController.class);

    @PostMapping("/insert")
    public BaseResult<Object> insertPost(@RequestBody PostDTO postDTO, @RequestHeader("Authorization") String token) throws ParseException {
        ThreadContext.setThreadLocal(token);
        logger.info("开始进行PO创建,DTO为: {}", postDTO);
        PostPO postPO = postService.assemblePO(postDTO);
        postService.publishPost(postPO);
        logger.info("PO创建完成");
        return BaseResult.success();
    }

    @PostMapping("/update")
    public BaseResult<Object> updatePost(@RequestBody UpdateDTO updateDto, @RequestHeader("Authorization") String token) {
        ThreadContext.setThreadLocal(token);
        postService.updatePost(updateDto);
        return BaseResult.success();
    }
    @DeleteMapping("/delete")
    public BaseResult<Object> deletePost(String uuid, @RequestHeader("Authorization") String token) {
        ThreadContext.setThreadLocal(token);
        postService.deletePost(uuid);
        return BaseResult.success();
    }
}