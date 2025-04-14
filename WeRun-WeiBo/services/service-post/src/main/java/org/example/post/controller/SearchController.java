package org.example.post.controller;

import org.example.common.model.global.BaseResult;
import org.example.post.service.SearchService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/post/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    @PostMapping("/keywords")
    public BaseResult<Object> searchPosts(String keyword, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        return BaseResult.success(searchService.searchPosts(keyword, page, pageSize));
    }
    @PostMapping("/tag")
    public BaseResult<Object> searchPostsByTag(String tagName, @RequestParam(defaultValue = "1")int page, @RequestParam(defaultValue = "10")int pageSize) {
        return BaseResult.success(searchService.searchPostsByTag(tagName, page, pageSize));
    }
}
