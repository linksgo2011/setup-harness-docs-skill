drop table if exists branch;
drop table if exists appointment;
drop table if exists consultant;
drop table if exists user;

create table user (
    id varchar(64) not null primary key,
    email varchar(128) not null,
    password varchar(256) not null,
    name varchar(64) not null,
    phone varchar(20),
    role varchar(20) not null default 'ROLE_USER',
    status varchar(12) not null default 'ACTIVE',
    deleted tinyint default 0 not null,
    created_by varchar(64) not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_by varchar(64) not null,
    updated_time datetime default CURRENT_TIMESTAMP not null
);

create unique index uk_user_email on user(email);

create table consultant (
    id varchar(64) not null primary key,
    name varchar(64) not null,
    title varchar(128),
    bio text,
    specialties varchar(256),
    avatar varchar(256),
    branch_id varchar(64),
    status varchar(12) not null default 'ACTIVE',
    sort_order int default 0 not null,
    created_by varchar(64) not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_by varchar(64) not null,
    updated_time datetime default CURRENT_TIMESTAMP not null
);

create table appointment (
    id varchar(64) not null primary key,
    user_id varchar(64) not null,
    consultant_id varchar(64) not null,
    branch_id varchar(64),
    appointment_date date not null,
    start_time varchar(10) not null,
    end_time varchar(10) not null,
    duration_minutes int not null,
    status varchar(20) not null default 'PENDING',
    note text,
    cancel_reason varchar(256),
    created_by varchar(64) not null,
    created_time datetime default CURRENT_TIMESTAMP not null,
    updated_by varchar(64) not null,
    updated_time datetime default CURRENT_TIMESTAMP not null
);

create index idx_appointment_user on appointment(user_id);
create index idx_appointment_consultant on appointment(consultant_id);
create index idx_appointment_date on appointment(appointment_date);
create unique index uk_appointment_slot on appointment(consultant_id, appointment_date, start_time);

create table branch (
    id varchar(64) primary key,
    name varchar(100) not null,
    address varchar(255),
    phone varchar(20),
    status varchar(20) not null default 'ACTIVE',
    created_by varchar(64),
    created_time datetime not null default CURRENT_TIMESTAMP,
    updated_by varchar(64),
    updated_time datetime not null default CURRENT_TIMESTAMP
);
