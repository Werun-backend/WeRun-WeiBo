package org.example.post.Service;


import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.PO.PostPO;
import org.example.post.POJO.DTO.UpdateDTO;

public interface PostService {

    void publishPost(PostPO postPO);
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


    PostPO assemblePO(PostDTO postDTO, String token);
}
