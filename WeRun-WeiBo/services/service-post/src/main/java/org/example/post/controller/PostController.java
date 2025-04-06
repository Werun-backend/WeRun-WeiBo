package org.example.post.controller;

import org.example.post.pojo.PO.PostPO;
import org.example.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("weibo/editpost")
@RestController
public class PostController {


        @Autowired
        private PostService postService;

        @GetMapping("/insert")
        public void insertPost(@RequestBody PostPO postPo, @RequestParam List<String> newtags, @RequestParam List<String> selectedtags) {
            postService.publishPost(postPo, newtags, selectedtags);
        }

        @PostMapping("/update")
        public void updatePost(@RequestBody PostPO postPo, @RequestParam List<String> deletetags, @RequestParam List<String> newtags, @RequestParam List<String> selectedtags) {
            postService.updatePost(postPo, deletetags, newtags, selectedtags);
        }



        @DeleteMapping("/{uuid}")
        public void deletePost(@PathVariable String uuid) {
            postService.deletePost(uuid);
        }

}
