# 数据字典规范

## 接口定义

所有业务枚举需实现 `Description` 接口：

```java
public interface Description {
    String getCode();             // enum.name()，如 "PENDING"
    String getDescription();      // 中文描述，如 "待确认"
    default Map<String, String> getExtraFields() { return Collections.emptyMap(); }
}
```

## 注册

枚举通过 `DataDictionaryType` 注册为字典类型：

```java
public interface DataDictionaryType {
    Class<? extends Description> getClazz();    // 枚举类
    String getDescription();                    // 字典类型描述
}
```

系统中实现 `DataDictionaryType` 的枚举统一管理所有字典注册。

## 动态查询

| 端点 | 说明 |
|------|------|
| `GET /api/v1/data-dictionaries` | 获取所有字典类型列表 |
| `GET /api/v1/data-dictionaries/{type}` | 获取指定字典的条目列表 |

## docs 同步

修改枚举后，运行 `DataDictionaryUtil` 的 `generateMarkdown()` 更新 `docs/dev/design-fact/others/data-dict.md`。
也可以在 AGENTS.md 中约定手动同步。
