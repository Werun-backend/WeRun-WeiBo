package org.example.comment.service;


import org.example.comment.pojo.dto.PostDTO;
import org.example.comment.pojo.po.PostPO;
import org.example.comment.pojo.dto.UpdateDTO;

import java.text.ParseException;

public interface PostService {

    void publishPost(PostPO postPO, String token);
//    void insertTag(String tagname);
//
//    void selectTagId(String tagname);
//
//    void insertPostTag(Long postid, Long tagid);

    void updatePost(UpdateDTO updateDto, String token);
    //    void insertTag(String tagname);
//    void selectTagId(String tagname);
//    void deletePostTag(Long postid, Long tagid);
    void deletePost(String uuid,String token);


    PostPO assemblePO(PostDTO postDTO, String token) throws ParseException;
}
