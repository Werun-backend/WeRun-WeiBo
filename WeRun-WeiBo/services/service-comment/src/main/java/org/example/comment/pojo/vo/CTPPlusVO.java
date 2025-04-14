package org.example.comment.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor

public class CTPPlusVO {
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    private int replyNum;
    private int isLiked;
    private Date createTime;
    private List<CTCVO> comments;
}
