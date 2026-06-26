# 业务规则规范

## 定义方式

业务规则通过 `IBusinessRule` 接口定义：

```java
public interface IBusinessRule {
    String getRuleName();       // 唯一规则名
    String getDescription();    // 描述
}
```

## 配置存储

- 规则配置存储在 `resources/business-rules/*.yml` 中
- 命名：`{domain}.yml`（如 `appointment.yml`、`auth.yml`）
- 格式：

```yaml
appointment:
  conflict:
    detection: "同一咨询师同一时段不可重复预约"
  slot:
    minMinutes: 30
    maxMinutes: 120
    intervalMinutes: 60
```

## 查询

- `BusinessRuleProvider` 在启动时加载所有 YAML 规则
- 通过 `GET /api/v1/business-rules` 公开查询

## 同步

新增或修改业务规则后，同步更新 `docs/dev/design-fact/others/businessrule.md`。
