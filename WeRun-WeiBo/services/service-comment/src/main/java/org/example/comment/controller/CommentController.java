package org.example.comment.controller;

import jakarta.validation.Valid;
import org.example.common.model.global.BaseResult;
import org.example.comment.pojo.dto.CTCDTO;
import org.example.comment.pojo.dto.CTPDTO;
import org.example.comment.pojo.dto.ReplyDTO;
import org.example.comment.service.CommentService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/comment")
@RestController
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/toPost")
    public BaseResult<Object> toPost(@Valid CTPDTO ctpdto, @RequestHeader("Authorization") String token){
        commentService.toPost(ctpdto,token);
        return BaseResult.success();
    }
    @PostMapping("/toComment")
    public BaseResult<Object> toComment(@Valid CTCDTO ctcdto, @RequestHeader("Authorization") String token){
        commentService.toComment(ctcdto,token);
        return BaseResult.success();
    }
    @PostMapping("/reply")
    public BaseResult<Object> reply(@Valid ReplyDTO replydto, @RequestHeader("Authorization") String token){
        commentService.reply(replydto,token);
        return BaseResult.success();
    }
    @GetMapping("/getCommentsByTime")
    public BaseResult<Object> getCommentsByTime(String postId){
        return BaseResult.success(commentService.getCommentsByTime(postId));
    }
    @GetMapping("/getCommentsByLikes")
    public BaseResult<Object> getCommentsByLikes(String postId){
        return BaseResult.success(commentService.getCommentsByLikes(postId));
    }
    @DeleteMapping("/deleteMyCommentsCTC")
    public BaseResult<Object> deleteMyCommentsCTC(String commentId, @RequestHeader("Authorization") String token){
        commentService.deleteMyCommentsCTC(commentId,token);
        return BaseResult.success();
    }
    @DeleteMapping("/deleteMyCommentsCTP")
    public BaseResult<Object> deleteMyCommentsCTP(String commentId, @RequestHeader("Authorization") String token){
        commentService.deleteMyCommentsCTP(commentId,token);
        return BaseResult.success();
    }
    @DeleteMapping("/deleteUnderPost")
    public BaseResult<Object> deleteUnderPost(String postId,String commentId, @RequestHeader("Authorization") String token){
        commentService.deleteUnderPost(postId,commentId,token);
        return BaseResult.success();
    }
    @PostMapping("/like")
    public BaseResult<Object> like(String commentId, @RequestHeader("Authorization") String token){
        commentService.like(commentId,token);
        return BaseResult.success();
    }
}
