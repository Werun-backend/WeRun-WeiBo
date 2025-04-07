package org.example.post.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.DTO.UpdateDto;
import org.example.post.POJO.PO.PostPO;

import java.util.List;

@Mapper
public interface PostMapper {
        // 插入帖子
        @Insert("INSERT INTO post (uuid, title, content, createtime, updatetime ，schedule" +
                "VALUES (#{uuid}, #{title}, #{content}, #{createTime}, #{updateTime},#{schedule})")
        void insertPost(PostDTO postPO);
        //更新帖子
        @Update("UPDATE post SET title = #{title}, content = #{content}, updatetime = #{updateTime} WHERE uuid = #{uuid}")
        void updatePost(UpdateDto updateDto);

        //删除帖子
        @Delete("DELETE FROM post WHERE uuid = #{uuid}")
        @Delete("DELETE FROM post_tags WHERE uuid = #{uuid}")
        void deletePost(String uuid);

        //展示所有标签
        @Select("SELECT * FROM tags")
        List<String> selectTags();

        // 插入标签 主键自增 返回id
        @Insert("INSERT IGNORE INTO tags (tagname) VALUES #{tagname}")
        Long insertTag(@Param("tagname") String tagname);

        // 查询标签 ID
        @Select("SELECT id FROM tags WHERE tagname = #{tagname}")
        Long selectTagId(@Param("tagname") String tagname);

        // 插入帖子标签关联
        @Insert("INSERT INTO post_tags (post_id, tag_id) VALUES (#{postid}, #{tagid})")
        void insertPostTag(@Param("postid") String postid, @Param("tagid") Long tagid);

        //删除 帖子关联标签
        @Delete("DELETE FROM post_tags WHERE postid = #{postid} AND tagid = #{tagid}")
        void deletePostTags(@Param("postId") Long postid, @Param("tagId") Long tagid);

//        //修改帖子关联标签
//        @Update("UPDATE post_tags SET tagid = #{tagid} WHERE postid = #{postid}")
//        void updatePostTags(@Param("postid") Long postid, @Param("tagid") Long tagid);

        // 查询帖子
        @Select("SELECT * FROM post WHERE uuid = #{uuid}")
        PostPO selectPostById(String uuid);
//
        // 查询帖子的标签
        @Select("SELECT t.tag_name FROM tags t JOIN post_tags pt ON t.id = pt.tagid WHERE pt.postid = #{postid}")
        List<String> selectTagsByPostId(Long postId);

        //根据用户id查询帖子
        @Select("SELECT * FROM post WHERE authid = #{authid}")
        List<PostPO> selectPostByAuthid(Long authid);


    }

