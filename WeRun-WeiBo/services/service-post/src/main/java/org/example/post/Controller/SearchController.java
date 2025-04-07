package org.example.post.Controller;

import org.example.post.POJO.PO.PostPO;
import org.example.post.Service.SearchService;
import org.example.post.POJO.VO.PageResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/weibo/search")
@RestController
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }
    @PostMapping("/keywords")
    public PageResult<PostPO> searchPosts(String keyword, int page, int pageSize) {
        return searchService.searchPosts(keyword, page, pageSize);
    }
    @PostMapping("/tag")
    public PageResult<PostPO> searchPostsByTag(String tagName, int page, int pageSize) {
        return searchService.searchPostsByTag(tagName, page, pageSize);
    }
}
