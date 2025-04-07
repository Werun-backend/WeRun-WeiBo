package org.example.post.Mapper;

import org.apache.ibatis.annotations.*;
import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.DTO.UpdateDto;
import org.example.post.POJO.PO.PostPO;

import java.util.List;

@Mapper
public interface PostMapper {
        // 插入帖子
        @Insert("INSERT INTO post (uuid, title, content, createTime, updateTime ,schedule,authorId" +
                "VALUES (#{uuid}, #{title}, #{content}, #{createTime}, #{updateTime},#{schedule},#{authorId})")
        void insertPost(PostDTO postDTO);
        //更新帖子
        @Update("UPDATE post SET title = #{title}, content = #{content}, updateTime = #{updateTime} WHERE uuid = #{uuid}")
        void updatePost(UpdateDto updateDto);

        //删除帖子
        @Delete("DELETE FROM post WHERE uuid = #{uuid}")
        @Delete("DELETE FROM post_tags WHERE uuid = #{uuid}")
        void deletePost(String uuid);

        //展示所有标签
        @Select("SELECT * FROM tags")
        List<String> selectTags();

//        // 插入标签 主键自增 返回id
//        @Insert("INSERT IGNORE INTO tags (tagname) VALUES #{tagname}")
//        Long insertTag(@Param("tagname") String tagname);
//
//        // 查询标签 ID
//        @Select("SELECT id FROM tags WHERE tagname = #{tagname}")
//        Long selectTagId(@Param("tagname") String tagname);

        // 插入帖子标签关联
        @Insert("INSERT INTO post_tags (postId, tagname) VALUES (#{postid}, #{tagname})")
        void insertPostTag(@Param("postid") String postid, @Param("tagname")String tagname);

        //删除 帖子关联标签
        @Delete("DELETE FROM post_tags WHERE postId = #{postid} AND tagname = #{tagname}")
        void deletePostTags(@Param("postid") String postid, @Param("tagname")String tagname );

//        //修改帖子关联标签
//        @Update("UPDATE post_tags SET tagid = #{tagid} WHERE postid = #{postid}")
//        void updatePostTags(@Param("postid") Long postid, @Param("tagid") Long tagid);

        // 查询帖子
        @Select("SELECT * FROM post WHERE uuid = #{uuid}")
        PostDTO  selectPostById(String uuid);

        //将定时帖子改为发送状态
        @Update("UPDATE post SET schedule = 2 WHERE uuid = #{uuid}")
        void updatePostSchedule(String uuid);
    }

