package org.example.post.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.post.POJO.BO.PostBO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SearchPostMapper {
    //根据输入的关键词查询帖子
    @Select("SELECT * FROM post WHERE title LIKE #{keyword} OR content LIKE #{keyword}")
    List<PostBO> searchPosts(String keyword);

    //根据输入的标签查询帖子
    @Select("SELECT p.* FROM post p JOIN post_tags pt ON p.uuid = pt.post_id WHERE pt.tagname LIKE CONCAT('%', #{tagname}, '%')")
    List<PostBO> searchPostsByTag(@Param("tagname") String tagname);
}
