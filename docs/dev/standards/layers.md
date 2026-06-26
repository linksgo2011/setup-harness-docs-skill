# 分层架构规范

## 四层架构

```
adapter → application → domain ← infrastructure
```

## adapter
HTTP Controller 入/出参协议转换、参数基础校验。不含业务逻辑。

## application
用例编排、事务边界管理、Command→domain→Response 转换（Assembler）。

## domain
核心业务逻辑、Entity/Aggregate/ValueObject、Repository 接口定义。不依赖 infrastructure。

## infrastructure
PO + Mapper + Converter + RepositoryImpl + 安全 + 配置。
