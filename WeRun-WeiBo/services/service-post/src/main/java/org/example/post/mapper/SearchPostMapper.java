package org.example.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.post.pojo.PO.PostPO;

import java.util.List;

@Mapper
public interface SearchPostMapper {
    //根据输入的关键词查询帖子
    @Select("SELECT * FROM post WHERE title LIKE #{keyword} OR content LIKE #{keyword}")
    List<PostPO> searchPosts(String keyword);

    //根据输入的标签查询帖子
    @Select("SELECT p.* FROM post p JOIN post_tags pt ON p.uuid = pt.post_id JOIN tags t ON pt.tag_id = t.id WHERE t.tag_name = #{tagName}")
    List<PostPO> searchPostsByTag(String tagName);
}
