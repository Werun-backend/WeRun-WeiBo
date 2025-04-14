package org.example.comment.service;

import jakarta.validation.Valid;
import org.example.comment.pojo.dto.CTCDTO;
import org.example.comment.pojo.dto.CTPDTO;
import org.example.comment.pojo.dto.ReplyDTO;
import org.example.comment.pojo.vo.CTPPlusVO;

import java.util.List;

public interface CommentService {
    void toPost(@Valid CTPDTO ctpdto,String token);

    void toComment(@Valid CTCDTO ctcdto, String token);

    void reply(@Valid ReplyDTO replydto, String token);

    List<CTPPlusVO> getCommentsByTime(String postId);

    void deleteMyCommentsCTC(String commentId, String token);

    void deleteMyCommentsCTP(String commentId, String token);

    void deleteUnderPost(String postId, String commentId,String token);

    void like(String commentId, String token);

    List<CTPPlusVO> getCommentsByLikes(String postId);
}
