package org.example.comment.service.impl;

import org.example.comment.clients.FindPostClient;
import org.example.comment.mapper.CommentMapper;
import org.example.comment.pojo.dto.CTCDTO;
import org.example.comment.pojo.dto.CTPDTO;
import org.example.comment.pojo.dto.ReplyDTO;
import org.example.comment.pojo.po.CTCPO;
import org.example.comment.pojo.po.CTPPO;
import org.example.comment.pojo.po.ReplyPO;
import org.example.comment.pojo.vo.CTPPlusVO;
import org.example.comment.pojo.vo.CTPVO;
import org.example.comment.service.CommentService;
import org.example.comment.utils.JwtUtils;
import org.example.comment.utils.RedisIdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    private final CommentMapper commentMapper;
    private final RedisIdWorker redisIdWorker;
    private final FindPostClient findPostClient;
    public CommentServiceImpl(CommentMapper commentMapper, RedisIdWorker redisIdWorker, FindPostClient findPostClient) {
        this.commentMapper = commentMapper;
        this.redisIdWorker = redisIdWorker;
        this.findPostClient = findPostClient;
    }
    Logger logger = LoggerFactory.getLogger(CommentServiceImpl.class);
    @Override
    public void toPost(CTPDTO ctpdto,String token) {
        logger.debug("正在进行对帖子评论操作，发帖传入的对象为:{},{}",ctpdto,token);
        CTPPO ctppo = new CTPPO(String.valueOf(redisIdWorker.nextId("CTP")), ctpdto.getPostId(), JwtUtils.getUserId(token), ctpdto.getContent());
        logger.debug("组装的帖子评论对象为:{}",ctppo);
        commentMapper.toPost(ctppo);
        logger.debug("帖子评论操作完成");
    }

    @Override
    public void toComment(CTCDTO ctcdto, String token) {
        logger.debug("正在进行对评论评论操作，评论传入的对象为:{}",ctcdto);
        String postId = commentMapper.getPostId(ctcdto.getPostedCommentId());
        CTCPO ctcpo = new CTCPO(String.valueOf(redisIdWorker.nextId("CTC")), ctcdto.getPostedCommentId(), postId,JwtUtils.getUserId(token), ctcdto.getContent());
        logger.debug("组装的评论评论对象为:{}",ctcpo);
        commentMapper.toComment(ctcpo);
        logger.debug("回复评论数目增加完成1");
        commentMapper.addCommentNum(JwtUtils.getUserId(token));
        logger.debug("评论评论操作完成");
    }

    @Override
    public void reply(ReplyDTO replydto, String token) {
        logger.debug("正在进行对回复评论操作，评论传入的对象为:{}",replydto);
        String postId = commentMapper.getPostId(replydto.getPostedCommentId());
        ReplyPO replyPO = new ReplyPO(String.valueOf(redisIdWorker.nextId("CTC")), replydto.getPostedCommentId(), postId, JwtUtils.getUserId(token), replydto.getContent(), replydto.getReplyId());
        logger.debug("组装的回复评论对象为:{}",replyPO);
        commentMapper.reply(replyPO);
        logger.debug("回复评论数目增加完成2");
        commentMapper.addCommentNum(JwtUtils.getUserId(token));
        logger.debug("回复评论操作完成");
    }

    @Override
    public List<CTPPlusVO> getCommentsByTime(String postId) {
        List<CTPVO> ctpvo = commentMapper.getCTP(postId);
        logger.debug("查询到的评论为:{}",ctpvo);
        List<CTPPlusVO> list = new ArrayList<>();
        for(CTPVO actpvo:ctpvo){
            CTPPlusVO ctpPlusVO = new CTPPlusVO(actpvo.getCommentId(), postId, actpvo.getUserId(), actpvo.getContent(), actpvo.getReplyNum(), actpvo.getIsLiked(), actpvo.getCreateTime(), commentMapper.getCTC(actpvo.getCommentId()));
            list.add(ctpPlusVO);
        }
        return list;
    }

    @Override
    public void deleteMyCommentsCTC(String commentId, String token) {
        logger.debug("正在进行对评论删除操作，评论id为:{}",commentId);
        commentMapper.deleteCTC(commentId,JwtUtils.getUserId(token));
    }

    @Override
    public void deleteMyCommentsCTP(String commentId, String token) {
        logger.debug("正在进行对评论删除操作，评论id为:{}",commentId);
        commentMapper.deleteCTP(commentId,JwtUtils.getUserId(token));
    }

    @Override
    public void deleteUnderPost(String postId, String commentId,String token) {
        if(findPostClient.checkPostsById(postId,JwtUtils.getUserId(token))==0){
            logger.error("你没有权限删除这个帖子");
            throw new RuntimeException("删除错误");
        }
        commentMapper.deleteUnderPost(postId,commentId);
    }

    @Override
    public void like(String commentId, String token) {
        commentMapper.like(commentId,JwtUtils.getUserId(token));
        logger.debug("点赞关系建立");
        commentMapper.addLikeNum(commentId);
    }

    @Override
    public List<CTPPlusVO> getCommentsByLikes(String postId) {
        List<CTPVO> ctpvo = commentMapper.getCTPByLikes(postId);
        logger.debug("查询到的评论为:{}",ctpvo);
        List<CTPPlusVO> list = new ArrayList<>();
        for(CTPVO actpvo:ctpvo){
            CTPPlusVO ctpPlusVO = new CTPPlusVO(actpvo.getCommentId(), postId, actpvo.getUserId(), actpvo.getContent(), actpvo.getReplyNum(), actpvo.getIsLiked(), actpvo.getCreateTime(), commentMapper.getCTC(actpvo.getCommentId()));
            list.add(ctpPlusVO);
        }
        return list;
    }
}
