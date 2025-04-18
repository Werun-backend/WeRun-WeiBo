package org.example.post.Mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.example.post.POJO.BO.PostBO;

import java.util.List;
@Mapper
public interface PushPostMapper {
    //查询所有帖子并按照时间顺序排序
    @Select("SELECT * FROM post ORDER BY create_time DESC")
    List<PostBO> selectAllPosts();
}
