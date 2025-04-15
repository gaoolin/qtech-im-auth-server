package com.qtech.im.auth.utils.web;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/15 08:53:07
 * desc   :  通用的分页响应类，确保JSON结构稳定。
 */

@Data
public class PageResponse<T> {
    private List<T> content;
    private int number;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean first;
    private boolean last;
    private boolean empty;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.first = page.isFirst();
        this.last = page.isLast();
        this.empty = page.isEmpty();
    }
}
