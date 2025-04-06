package org.example.post.controller;

import org.example.post.pojo.PO.PostPO;
import org.example.post.service.SearchService;
import org.example.post.serviceimpl.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "weibo/search")
@RestController
public class SearchController {
    @Autowired
    private SearchService searchService;
    @PostMapping("keyywords")
    public PageResult<PostPO> searchPosts(String keyword, int page, int pageSize) {
        return searchService.searchPosts(keyword, page, pageSize);
    }
    @PostMapping("tag")
    public PageResult<PostPO> searchPostsByTag(String tagName, int page, int pageSize) {
        return searchService.searchPostsByTag(tagName, page, pageSize);
    }

}
