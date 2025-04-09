package org.example.post.Controller;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.DTO.UpdateDTO;
import org.example.post.Service.PostService;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/post/editPost")
@RestController
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping("/insert")
    public void insertPost(@RequestBody PostDTO postDTO,@RequestHeader("Authorization") String token){
        PostPO postPO = postService.assemblePO(postDTO, token);
        postService.publishPost(postPO);
    }

    @PostMapping("/update")
    public void updatePost(@RequestBody UpdateDTO updateDto) {
        postService.updatePost(updateDto);
    }

    @DeleteMapping("/delete")
    public void deletePost(String uuid) {
        postService.deletePost(uuid);
        }
}
