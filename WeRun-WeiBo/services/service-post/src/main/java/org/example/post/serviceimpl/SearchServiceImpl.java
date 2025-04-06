package org.example.post.serviceimpl;

import lombok.extern.slf4j.Slf4j;
import org.example.post.mapper.SearchPostMapper;
import org.example.post.pojo.PO.PostPO;
import org.example.post.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Slf4j
public class SearchServiceImpl implements SearchService {
    @Autowired
    public SearchPostMapper searchPostMapper;
    //根据关键词查询
    @Override
    public PageResult<PostPO> searchPosts(String keyword, int page, int pageSize) {
            log.debug("正在进行搜索帖子服务");
        if (page <= 0 || pageSize <= 0) {
            // 如果页码或页面大小小于等于0，则抛出异常
            log.error("帖子分页发生错误");
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        try {
            log.debug("正在从DAO层获取数据");
            List<PostPO> list = searchPostMapper.searchPosts(keyword);
            log.debug("数据获取完成");
            int total = list.size();
            return new PageResult<>(total, list, page, pageSize);
        } catch (Exception e) {
            log.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }

        }




    @Override
    public PageResult<PostPO> searchPostsByTag(String tagName, int page, int pageSize){
        if (page <= 0 || pageSize <= 0) {
            log.error("帖子分页发送错误");
            // 如果页码或页面大小小于等于0，则抛出异常
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        try {
            log.debug("正在从DAO层获取数据，利用tag");
            List<PostPO> list = searchPostMapper.searchPostsByTag(tagName);
            int total = list.size();
            return new PageResult<>(total, list, page, pageSize);

        } catch (Exception e) {
            log.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }
        }
    }



