package org.example.comment.POJO.VO;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CTPVO {
    private String commentId;
    private String postId;
    private String userId;
    private String content;
    private int status;
    private int replyNum;
    private int isLiked;
    private Date createTime;
//    private List<CTCVO> comments;
}
