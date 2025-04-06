package org.example.post.service;


import org.example.post.mapper.PostMapper;
import org.example.post.pojo.DTO.UpdateDto;
import org.example.post.pojo.PO.PostPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface PostService {

    void publishPost(PostPO postPo, List<String> newtags, List<String> selectedtags);
//    void insertTag(String tagname);
//
//    void selectTagId(String tagname);
//
//    void insertPostTag(Long postid, Long tagid);

    void updatePost(UpdateDto updateDto, List<String> deletetags, List<String> newtags, List<String> selectedtags);
    //    void insertTag(String tagname);
//    void selectTagId(String tagname);
//    void deletePostTag(Long postid, Long tagid);
    void deletePost(String uuid);

//    void insertTag(String tagname);
//
//    void selectTagId(String tagname);
//
//    void insertPostTag(Long postid, Long tagid);
//
//    void deletePostTag(Long postid, Long tagid);
//
//    void updatePostTag(Long postid, Long tagid);
    PostPO getPostById(String uuid);

}
