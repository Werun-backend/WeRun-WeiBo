package org.example.post.POJO.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class UpdateDto {
    private String uuid;
    private String title;
    private String content;
    private Set<String> tags;
    private LocalDateTime updatedate;
}
