create table consultant
(
    id           varchar(64)   not null primary key comment '咨询师ID',
    name         varchar(64)   not null comment '姓名',
    title        varchar(128)  null comment '头衔',
    bio          text          null comment '简介',
    specialties  varchar(256)  null comment '专长',
    avatar       varchar(256)  null comment '头像',
    branch_id    varchar(64)   null comment '网点ID',
    status       varchar(12)   not null default 'ACTIVE' comment '状态',
    sort_order   int           default 0 not null comment '排序',
    created_by   varchar(64)   not null,
    created_time datetime      default CURRENT_TIMESTAMP not null,
    updated_by   varchar(64)   not null,
    updated_time datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment '咨询师表' charset = utf8mb4;

create index idx_consultant_status on consultant(status);

insert into consultant (id, name, title, bio, specialties, sort_order, created_by, updated_by)
values ('c-001', '张医生', '资深心理咨询师', '拥有15年心理咨询经验。', '焦虑症,抑郁症,人际关系', 1, 'admin-001', 'admin-001'),
       ('c-002', '李律师', '企业法律顾问', '10年企业法律顾问经验。', '合同法,劳动法,公司治理', 2, 'admin-001', 'admin-001'),
       ('c-003', '王教练', '职业规划导师', '帮助超过500人实现职业转型。', '职业规划,简历优化', 3, 'admin-001', 'admin-001');

create table appointment
(
    id               varchar(64)   not null primary key comment '预约ID',
    user_id          varchar(64)   not null comment '用户ID',
    consultant_id    varchar(64)   not null comment '咨询师ID',
    branch_id        varchar(64)   null comment '网点ID',
    appointment_date date          not null comment '预约日期',
    start_time       varchar(10)   not null comment '开始时间',
    end_time         varchar(10)   not null comment '结束时间',
    duration_minutes int           not null comment '时长(分钟)',
    status           varchar(20)   not null default 'PENDING' comment '状态',
    note             text          null comment '备注',
    cancel_reason    varchar(256)  null comment '取消原因',
    created_by       varchar(64)   not null,
    created_time     datetime      default CURRENT_TIMESTAMP not null,
    updated_by       varchar(64)   not null,
    updated_time     datetime      default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP
) comment '预约表' charset = utf8mb4;

create index idx_appointment_user on appointment(user_id);
create index idx_appointment_consultant on appointment(consultant_id);
create index idx_appointment_date on appointment(appointment_date);
create unique index uk_appointment_slot on appointment(consultant_id, appointment_date, start_time);
