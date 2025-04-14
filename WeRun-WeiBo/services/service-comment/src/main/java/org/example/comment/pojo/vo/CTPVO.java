package org.example.comment.pojo.vo;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CTPVO {
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    private int replyNum;
    private int isLiked;
    private Date createTime;
    private List<CTCVO> comments;
}
