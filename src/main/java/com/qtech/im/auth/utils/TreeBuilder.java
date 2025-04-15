package com.qtech.im.auth.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * author :  gaozhilin
 * email  :  gaoolin@gmail.com
 * date   :  2025/04/14 11:23:51
 * desc   :  通用树构建工具类（GenericTreeBuilder）
 */
public class TreeBuilder {

    public static <T extends TreeNode<T>> List<T> buildTree(List<T> nodes, Long rootId) {
        // 使用 Map 存储节点 ID 和节点对象的映射
        Map<Long, T> idToNodeMap = nodes.stream()
                                        .collect(Collectors.toMap(TreeNode::getId, node -> node));
        List<T> tree = new ArrayList<>();

        // 构建树
        for (T node : nodes) {
            if (Objects.equals(node.getParentId(), rootId)) {
                tree.add(node);
            } else {
                T parent = idToNodeMap.get(node.getParentId());
                if (parent != null) {
                    parent.getChildren().add(node);  // 此时 parent.getChildren() 的类型为 List<T>
                }
            }
        }

        return tree;
    }
}
