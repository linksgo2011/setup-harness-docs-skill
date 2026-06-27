# Snippet — 常见业务代码模版

存放 DDD 四层架构的代码打样，供新功能开发时参考复制。所有模版均来自本项目的实现代码。

## 目录

| # | 文件 | 层 | 用途 |
|---|------|-----|------|
| 1 | `01-ddd-controller.md` | Adapter | REST Controller 模版 |
| 2 | `02-ddd-appservice.md` | Application | AppService 用例编排模版 |
| 3 | `03-ddd-assembler.md` | Application | Domain → DTO 转换器模版 |
| 4 | `04-ddd-aggregate.md` | Domain | 聚合根模版（含业务守卫方法） |
| 5 | `05-ddd-domain-service.md` | Domain | Domain Service 模版 |
| 6 | `06-ddd-repository-interface.md` | Domain | Repository 接口模版 |
| 7 | `07-ddd-repository-impl.md` | Infrastructure | MyBatis-Plus Repository 实现模版 |
| 8 | `08-ddd-converter.md` | Infrastructure | PO ⟷ Domain 转换器模版 |
| 9 | `09-ddd-po-mapper.md` | Infrastructure | PO 与 Mapper 模版 |
| 10 | `10-global-exception-handler.md` | Infrastructure | 全局异常处理模版 |
