package org.example.post.Service;


import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.DTO.UpdateDto;
import org.example.post.POJO.PO.PostPO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service

public interface PostService {

    void publishPost(PostDTO postDTO);
//    void insertTag(String tagname);
//
//    void selectTagId(String tagname);
//
//    void insertPostTag(Long postid, Long tagid);

    void updatePost(UpdateDto updateDto);
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
    PostDTO getPostById(String uuid);

}
