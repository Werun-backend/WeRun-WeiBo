package org.example.post.service;

import org.example.post.pojo.PO.PostPO;

import java.util.List;

public interface ScheduleService {
    // 添加定时帖子
    void schedulePost(PostPO postPO, List<String> newtags, List<String> selectedTags, long executeTime);

    //扫描并执行任务
    void scanAndExecuteTask();

}
