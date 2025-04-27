package org.example.comment.pojo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReplyDTO {
    @NotNull(message = "正确指定所发的评论")
    private String postedCommentId;
    @NotNull(message = "所发内容不可以为空")
    private String content;
    @NotNull(message = "被回复者不可为空")
    private String replyId;
}
