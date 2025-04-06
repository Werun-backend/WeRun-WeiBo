package org.example.post.pojo.DTO;
import java.util.Set;
import java.time.LocalDateTime;
public class UpdateDto {
    private String title;
    private String content;
    private Set<String> tags;
    private LocalDateTime updatedat;
}
