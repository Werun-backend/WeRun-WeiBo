package org.example.comment.pojo.bo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CTPBO {
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    private int replyNum;
    private int isLiked;
    private int status;
    private Date createTime;
}
