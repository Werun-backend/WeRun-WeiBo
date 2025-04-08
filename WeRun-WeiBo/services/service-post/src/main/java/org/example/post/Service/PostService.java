package org.example.post.Service;


import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.DTO.UpdateDTO;
import org.springframework.stereotype.Service;

public interface PostService {

    void publishPost(PostDTO postDTO);
//    void insertTag(String tagname);
//
//    void selectTagId(String tagname);
//
//    void insertPostTag(Long postid, Long tagid);

    void updatePost(UpdateDTO updateDto);
    //    void insertTag(String tagname);
//    void selectTagId(String tagname);
//    void deletePostTag(Long postid, Long tagid);
    void deletePost(String uuid);


}
