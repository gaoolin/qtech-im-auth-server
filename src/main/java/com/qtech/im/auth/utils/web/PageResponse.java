package com.qtech.im.auth.utils.web;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.Collections;
import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/15 08:53:07
 * desc   :  通用的分页响应类，确保JSON结构稳定。
 */

/**
 * 通用的分页响应类，确保JSON结构稳定。
 * <p>
 * 该类用于封装分页查询的结果，提供统一的响应格式。
 * 包含分页数据、当前页码、总页数等信息。
 *
 * @param <T> 分页内容的类型
 */
@Getter
public final class PageResponse<T> {
    /**
     * 分页内容列表
     */
    private final List<T> content;

    /**
     * 当前页码（从0开始）
     */
    private final int number;

    /**
     * 每页大小
     */
    private final int size;

    /**
     * 总页数
     */
    private final int totalPages;

    /**
     * 总元素数量
     */
    private final long totalElements;

    /**
     * 是否为第一页
     */
    private final boolean first;

    /**
     * 是否为最后一页
     */
    private final boolean last;

    /**
     * 是否为空分页
     */
    private final boolean empty;

    private PageResponse(List<T> content, int number, int size, int totalPages, long totalElements, boolean first, boolean last, boolean empty) {
        this.content = Collections.unmodifiableList(content);
        this.number = number;
        this.size = size;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.first = first;
        this.last = last;
        this.empty = empty;
    }

    /**
     * 构造函数，基于Spring Data的Page对象初始化分页响应。
     *
     * @param page Spring Data的Page对象
     * @throws IllegalArgumentException 如果page为null
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        if (page == null) {
            throw new IllegalArgumentException("Page must not be null");
        }
        return new PageResponse<>(
                page.getContent(),
                page.getNumber(),
                page.getSize(),
                page.getTotalPages(),
                page.getTotalElements(),
                page.isFirst(),
                page.isLast(),
                page.isEmpty()
        );
    }
}
