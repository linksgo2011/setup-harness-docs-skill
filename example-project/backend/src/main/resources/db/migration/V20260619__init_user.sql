create table user
(
    id           varchar(64)   not null primary key comment '用户ID',
    email        varchar(128)  not null comment '邮箱',
    password     varchar(256)  not null comment '密码(BCrypt)',
    name         varchar(64)   not null comment '姓名',
    phone        varchar(20)   null comment '手机号',
    role         varchar(20)   not null default 'ROLE_USER' comment '角色',
    status       varchar(12)   not null default 'ACTIVE' comment '状态',
    deleted      tinyint(1)    default 0 not null,
    created_by   varchar(64)   not null,
    created_time datetime      default CURRENT_TIMESTAMP not null,
    updated_by   varchar(64)   not null,
    updated_time datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment '用户表' charset = utf8mb4;

create unique index uk_user_email on user(email);

insert into user (id, email, password, name, phone, role, status, created_by, updated_by)
values ('admin-001', 'admin@test.com',
        '$2a$10$4rDXdT56xLoF0LajF1fKRuZFxxdAd6rM9CuCcZdRrb/k7eRHMTKQq',
        '管理员', '13800000000', 'ROLE_ADMIN', 'ACTIVE', 'admin-001', 'admin-001');
