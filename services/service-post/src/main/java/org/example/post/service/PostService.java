package org.example.post.service;


import org.example.post.pojo.dto.PostDTO;
import org.example.post.pojo.po.PostPO;
import org.example.post.pojo.dto.UpdateDTO;

import java.text.ParseException;

public interface PostService {

    void publishPost(PostPO postPO);


    void updatePost(UpdateDTO updateDto);


    void deletePost(String uuid);


    PostPO assemblePO(PostDTO postDTO) throws ParseException;
}
