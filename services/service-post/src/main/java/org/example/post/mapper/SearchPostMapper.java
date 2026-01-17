package org.example.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.*;
import org.example.post.pojo.vo.PostVO;

import java.util.List;

@Mapper
public interface SearchPostMapper {

    //根据输入的关键词查询帖子
    List<PostVO> searchPosts(String keyword);

    //根据输入的标签查询帖子
    List<PostVO> searchPostsByTag(String tagName);

    //根据输入的id查询帖子
    PostVO searchPostsById(String uuid);

    //查找数量
    int checkPostsById(String uuid, String authorId);
}
