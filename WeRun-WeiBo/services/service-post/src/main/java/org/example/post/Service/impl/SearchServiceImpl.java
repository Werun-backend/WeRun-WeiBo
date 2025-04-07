package org.example.post.Service.impl;

import org.example.post.Mapper.SearchPostMapper;
import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.VO.PageResult;
import org.example.post.Service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SearchServiceImpl implements SearchService {

    private final SearchPostMapper searchPostMapper;

    public SearchServiceImpl(SearchPostMapper searchPostMapper) {
        this.searchPostMapper = searchPostMapper;
    }
    Logger logger = LoggerFactory.getLogger(SearchServiceImpl.class);
    //根据关键词查询
    @Override
    public PageResult<PostPO> searchPosts(String keyword, int page, int pageSize) {
            logger.debug("正在进行搜索帖子服务");
        if (page <= 0 || pageSize <= 0) {
            // 如果页码或页面大小小于等于0，则抛出异常
            logger.error("帖子分页发生错误");
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        try {
            logger.debug("正在从DAO层获取数据");
            List<PostPO> list = searchPostMapper.searchPosts(keyword);
            logger.debug("数据获取完成");
            int total = list.size();
            return new PageResult<>(total, list, page, pageSize);
        } catch (Exception e) {
            logger.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }

        }




    @Override
    public PageResult<PostPO> searchPostsByTag(String tagName, int page, int pageSize){
        if (page <= 0 || pageSize <= 0) {
            logger.error("帖子分页发送错误");
            // 如果页码或页面大小小于等于0，则抛出异常
            throw new IllegalArgumentException("Page and pageSize must be greater than 0");
        }
        try {
            logger.debug("正在从DAO层获取数据，利用tag");
            List<PostPO> list = searchPostMapper.searchPostsByTag(tagName);
            int total = list.size();
            return new PageResult<>(total, list, page, pageSize);

        } catch (Exception e) {
            logger.error("Error occurred while fetching user posts", e);
            throw new RuntimeException("Failed to fetch user posts", e);
        }
        }
    }



