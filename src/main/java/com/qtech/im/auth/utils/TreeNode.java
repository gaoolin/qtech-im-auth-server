package com.qtech.im.auth.utils;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/14 11:25:18
 * desc   :
 */
public interface TreeNode<T> {
    Long getId();
    Long getParentId();
    List<T> getChildren();  // 改为返回 List<T> 类型
}