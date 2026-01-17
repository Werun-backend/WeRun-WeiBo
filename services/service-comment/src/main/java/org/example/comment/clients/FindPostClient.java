package org.example.comment.clients;

import org.example.common.model.global.BaseResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("service-post")
public interface FindPostClient {
    @GetMapping("/post/search/check")
    BaseResult<Integer> checkPostsById(@RequestParam("uuid") String uuid, @RequestParam("authorId") String authorId);
}
