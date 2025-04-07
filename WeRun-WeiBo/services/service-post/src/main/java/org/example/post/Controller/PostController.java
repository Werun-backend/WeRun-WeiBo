package org.example.post.Controller;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.DTO.UpdateDto;
import org.example.post.POJO.PO.PostPO;
import org.example.post.Service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("weibo/editPost")
@RestController
public class PostController {

    private final PostService postService;
    public PostController(PostService postService) {
        this.postService = postService;
    }
    @PostMapping("/insert")
    public void insertPost(@RequestBody PostDTO postDTO) {
        postService.publishPost(postDTO);
    }

    @PostMapping("/update")
    public void updatePost(@RequestBody UpdateDto updateDto, @RequestParam List<String> deleteTags, @RequestParam List<String> newTags, @RequestParam List<String> selectedTags) {
        postService.updatePost(updateDto, deleteTags, newTags, selectedTags);
    }

    @DeleteMapping("/{uuid}")
    public void deletePost(@PathVariable String uuid) {
            postService.deletePost(uuid);
        }
}
