package org.example.post.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.post.pojo.PO.PostPO;
import org.example.post.pojo.VO.UserPage;

import java.util.List;

@Mapper
public interface UserPostMapper {
    //根据用户ID查找帖子并分页展示
    @Select("SELECT * FROM post WHERE authid = #{authid} LIMIT #{offset}, #{limit}")
    List<PostPO> selectUserPosts(@Param("authid") String authid, @Param("offset") int offset, @Param("limit") int limit);

    //统计总帖子数量
    @Select("SELECT COUNT(*) FROM post WHERE authid = #{authid}")
    int countUserPosts(@Param("authid") String authid);
}
