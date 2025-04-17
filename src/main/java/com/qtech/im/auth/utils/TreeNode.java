package com.qtech.im.auth.utils;

import java.util.List;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/14 11:25:18
 * desc   :  TreeNode<T> 接口的设计核心是：用最小的抽象，最大限度地支持不同类型的树结构统一操作，保持强类型、安全、解耦、可复用。
 */
public interface TreeNode<T> {
    Long getId();
    Long getParentId();
    List<T> getChildren();  // 改为返回 List<T> 类型
}