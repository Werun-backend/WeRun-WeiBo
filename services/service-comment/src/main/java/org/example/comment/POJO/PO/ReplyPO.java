package org.example.comment.POJO.PO;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyPO{
    private String commentId;
    private String postedCommentId;
    private String postId;
    private String userId;
    private String content;
    private String replyId;
}
