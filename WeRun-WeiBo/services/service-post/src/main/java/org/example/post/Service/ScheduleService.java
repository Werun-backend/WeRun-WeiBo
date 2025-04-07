package org.example.post.Service;

import org.example.post.POJO.DTO.PostDTO;
import org.example.post.POJO.PO.PostPO;

import java.util.List;

public interface ScheduleService {
    // 添加定时帖子
    void schedulePost(PostDTO postDTO, long executeTime);

    //扫描并执行任务
    void scanAndExecuteTask();

    // 更新帖子状态
    void updatePostSchedule(String uuid);
}
