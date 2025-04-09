package org.example.post.POJO.BO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostBO {
    private  String uuid;
    private  String authorId;
    private  String title;
    private  String content;
    private  LocalDateTime createTime;
    private  LocalDateTime updateTime;
    private  Integer commentCount;
    private  Integer schedule;
    private  LocalDateTime scheduleTime;
}
