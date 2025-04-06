package org.example.post.pojo.PO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostPO {
    private  String uuid;
    private  String authorId;
    private  String title;
    private  String content;
    //Data类型不可用，需要使用LocalDateTime？
    private  LocalDateTime createtime;
    private  LocalDateTime updatetime;
    private  Integer commentcount;
    //0表示不定时，1表示定时，2表示已发布
    private  Integer schedule;
    //定时发布的时间
    private  LocalDateTime scheduleTime;
}
