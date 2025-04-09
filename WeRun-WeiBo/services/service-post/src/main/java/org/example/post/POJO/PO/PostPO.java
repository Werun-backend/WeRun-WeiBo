package org.example.post.POJO.PO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostPO {
    private String uuid;
    private String authorId;
    private String title;
    private String content;
    private Integer schedule;
    private LocalDateTime scheduleTime;
    private List<String> tags;
}
