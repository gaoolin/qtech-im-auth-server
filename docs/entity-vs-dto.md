# 实体类与 DTO 分离的示例和说明
# 实体类与 DTO 分离规范说明

> 本文档解释为什么推荐在项目中使用 DTO（Data Transfer Object）模式来替代实体类（如 Department）与前端进行数据交互，并给出规范性使用建议与示例。

---

## 一、为什么不能直接暴露实体类给前端？

在 Spring Boot 项目中，实体类（Entity）通常用于数据库交互，如 ORM 映射，它包含数据库字段和结构信息。如果直接将实体类用于前端交互，可能会带来以下问题：

### 1. 安全性问题

- 实体类中可能包含敏感字段（如密码、权限等级、内部状态），直接暴露存在泄漏风险。
- 控制字段暴露粒度困难（如某些字段前端根本不需要看到）。

### 2. 灵活性问题

- 前端可能需要的字段格式与数据库不一致，例如：
  - 字段名不同
  - 需要组合字段或嵌套结构
  - 日期、枚举等需要转换格式
- 使用 DTO 后，可以自由组装数据结构而不影响数据库模型。

### 3. 可维护性问题

- 实体类变化会波及到前端接口，破坏解耦。
- 开发过程中频繁改动实体类结构会增加耦合和回归风险。

---

## 二、什么是 DTO（Data Transfer Object）？

DTO 是用于数据传输的对象，通常位于：

com.qtech.im.auth.dto

DTO 只关注**与前端交互的数据结构**，可以手动或使用 MapStruct、BeanUtils 等工具从实体类转换得到。

---

## 三、项目实践建议

| 项目层 | 实体类使用 | DTO 使用 |
|--------|------------|----------|
| Repository | ✅ 是，实体类 | ❌ 否 |
| Service | ✅ 是，内部处理 | ✅ 是，DTO 出入参 |
| Controller | ❌ 否 | ✅ DTO 作为出入参 |
| 前端 | ❌ 绝不直接对接实体类 | ✅ 与 DTO 对接 |

---

## 四、示例对比

### 4.1 实体类 `Department.java`

```java
public class Department {
    private Long id;
    private String deptName;
    private Long parentId;
    private String leader;
    private String phone;
    private String email;
    private String remark;
    private Integer orderNum;
    private Boolean deleted;
    private LocalDateTime createTime;
}
```
### 4.2 DTO `DepartmentDTO.java`
```java
public class DepartmentDTO {
    private Long id;
    private String deptName;
    private Long parentId;
    private String leader;
    private String phone;
    private String email;
    private String remark;
    private Integer orderNum;
    private List<DepartmentDTO> children; // 用于构建树形结构
}
```
✅ 这个 DTO 就是对实体类的简化与适配，去除了不需要暴露给前端的字段，也添加了前端所需的嵌套结构。

### 五、DTO 常用命名约定
| 类型 | 命名示例 | 说明 |
|----|------------|----------|
| 请求 DTO | CreateDeptRequestDTO | 表单提交时的参数封装 |
| 响应 DTO | DeptResponseDTO | 返回给前端的数据结构 |
| 通用 DTO | DepartmentDTO | 适配树、表格等通用场景 |
| 转换工具类 | DeptMapper | 用于实体类与 DTO 之间的转换（建议使用 MapStruct）|

---
## 六、推荐结构
```
src/
└── main/
    └── java/
        └── com/qtech/im/auth/
            ├── controller/
            ├── service/
            ├── repository/
            ├── model/              # 存放实体类
            │   └── Department.java
            └── dto/                # 存放所有 DTO 类
                └── DepartmentDTO.java
```

--- 
## 七、转换工具建议
推荐使用 MapStruct 来实现 DTO ↔ Entity 的自动映射，示例：
```java
@Mapper(componentModel = "spring")
public interface DeptMapper {
    DepartmentDTO toDto(Department dept);
    Department toEntity(DepartmentDTO dto);
}
```

---
## 八、结语
* 实体类与 DTO 分离，是规范化开发、提高系统安全性和可维护性的关键一步。建议在项目初期就明确实体类和 DTO 的使用边界，为后续系统扩展、前后端协作打下良好基础。
* 职责清晰（DTO 传输、Entity 存储）
* DTO 是你和前端之间的“契约”，而实体类是你和数据库之间的“约定”，两者职责不同，最好分开维护。