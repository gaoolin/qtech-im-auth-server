# 通用树形结构中的泛型设计规范

## 背景

在构建如部门树、菜单树等层级结构数据时，常常会使用递归或 Tree 模型。为了实现通用性，我们希望通过泛型定义统一的树形节点接口 `TreeNode` 并构建通用的 `TreeBuilder`。

但在实际开发中，使用泛型的方式若不当，可能会导致 IDE 报错或无法添加子节点的问题。

---

## 错误用法示例：使用 `? extends TreeNode`

```java
public interface TreeNode {
    Long getId();
    Long getParentId();
    List<? extends TreeNode> getChildren();
}
```
## ❌ 问题
如果 getChildren() 返回的是 List<? extends TreeNode>，将导致不能执行如下代码：
```
parent.getChildren().add(child); // 编译错误 ❌
```
原因是 ? extends TreeNode 表示协变（Covariant），Java 无法确保你添加的元素类型与 ? 匹配，因而禁止添加。

---
## 正确用法：使用泛型参数绑定
```java
public interface TreeNode<T> {
    Long getId();
    Long getParentId();
    List<T> getChildren();
}
```
然后在具体实现类中进行泛型绑定：
```java
public class DeptTree implements TreeNode<DeptTree> {
    private Long id;
    private Long parentId;
    private List<DeptTree> children = new ArrayList<>();

    // Getter 和 Setter

    @Override
    public List<DeptTree> getChildren() {
        return children;
    }
}
```
此时构建树结构的代码将能正常工作：
```
parent.getChildren().add(child); // 编译通过 ✅
```

---
## 总结对比
|写法 | 是否可读取 | 是否可添加 | 推荐场景 |
| --- | --- | --- | --- |
| List<? extends TreeNode> | ✅ 是 | ❌ 否 | 只读展示、API 返回结构 |
| List<T>（T extends TreeNode） | ✅ 是 | ✅ 是 | 构建/编辑树结构时 ✅ |

## 建议
在需要构建树结构的类中，请使用带泛型参数的 TreeNode<T> 接口，并确保 getChildren() 返回的类型为 List<T>，以支持子节点的添加。

---
## 拓展
像 List<? extends TreeNode> 这样的用法属于 Java 泛型的协变（covariant）表达方式，适合用于只读访问的场景。我们可以从“用途”和“设计目的”两个方面理解它的使用场景。

---
### ✅ 一、使用 List<? extends T> 的典型场景
适用于只读取数据、不修改集合内容的情况。
1. 示例 1：作为方法参数，只读使用
```
public void printNodeNames(List<? extends TreeNode> nodes) {
    for (TreeNode node : nodes) {
        System.out.println(node.getId());
    }
}
```
🔸解释：
你可以传入 List<MenuNode>、List<DeptTree> 等 TreeNode 子类列表，方法内部不会修改集合，只读取数据，所以是安全的。
2. 示例 2：作为 API 的返回值，只展示结构
```
public List<? extends TreeNode> getReadOnlyTree();
```
🔸解释：
调用者可以遍历使用这个树结构，但 不能往集合中添加新的节点。适合做“只读视图”，可以防止误改原始数据。

### 🚫 二、什么时候不适合？
* 你需要 向集合中添加元素

* 你需要对集合进行结构变更（比如添加、删除）

此时就不应使用 List<? extends T>，因为 Java 编译器无法保证类型安全，也不允许你添加元素。

---
### 🧠 三、记忆口诀
|泛型写法|是否可读|是否可写|适合场景|
| --- | --- | --- | --- |
|List<? extends T>|✅ 是|❌ 否|只读、展示结构|
|List<? super T>|❌ 不一定|✅ 是|向上添加，作为“收集器”|
|List<T>|✅ 是|✅ 是|可读可写，结构操作|

---
### 🔄 四、图示帮助理解
```
                TreeNode
               /        \
           DeptTree    MenuTree

你用 List<? extends TreeNode> 时，可以接受：
- List<DeptTree>
- List<MenuTree>
但不能往里面 add new DeptTree()，因为你无法确定泛型确切类型

```

---
### ✅ 总结
使用 List<? extends TreeNode> 的最佳时机是：

* 你不打算修改这个列表（不新增、不删除元素）

* 你希望这个方法更灵活地接受多种子类类型

* 你是在做“输出”、“只读遍历”而不是“结构变更”

* 这是一种泛型类型安全的妥协设计 —— 保证灵活性但牺牲写能力。
