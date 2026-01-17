package org.example.post.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;
import java.util.Set;

@Data
@AllArgsConstructor

public class PostVO {
    private String uuid;
    private String authorId;
    private String title;
    private String content;
    private Date createTime;
    private Date updateTime;
    private Integer commentCount;
    private Set<String> Tags;
}
