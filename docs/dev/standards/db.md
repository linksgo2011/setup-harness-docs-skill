# 数据库规范

- 字符集 utf8mb4，排序 utf8mb4_bin，引擎 InnoDB
- 表名/字段小写蛇形，表名单数，主键 VARCHAR(64) UUID
- 公共字段：created_by, created_time, updated_by, updated_time
- Flyway 迁移 V<YYYYMMDD>__<description>.sql
