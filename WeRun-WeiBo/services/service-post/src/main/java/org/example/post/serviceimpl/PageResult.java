package org.example.post.serviceimpl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
@AllArgsConstructor
@NoArgsConstructor

public class PageResult<T> {
    private int total; // 总记录数
    private List<T> list; // 当前页的数据
    private int page; // 当前页码
    private int pageSize; // 每页大小

}