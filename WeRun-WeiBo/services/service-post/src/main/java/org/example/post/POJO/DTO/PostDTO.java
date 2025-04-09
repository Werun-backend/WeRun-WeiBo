package org.example.post.POJO.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
public class PostDTO {
    private String title;
    private String content;
    private Integer schedule;
    private LocalDateTime scheduleTime;
    private List<String> tags;
}
