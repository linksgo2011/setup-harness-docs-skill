-- Seed admin user (password: password)
insert into user (id, email, password, name, phone, role, status, created_by, updated_by)
values ('admin-001', 'admin@test.com',
        '$2a$10$4rDXdT56xLoF0LajF1fKRuZFxxdAd6rM9CuCcZdRrb/k7eRHMTKQq',
        '管理员', '13800000000', 'ROLE_ADMIN', 'ACTIVE', 'admin-001', 'admin-001');

-- Seed consultants
insert into consultant (id, name, title, bio, specialties, sort_order, created_by, updated_by)
values ('c-001', '张医生', '资深心理咨询师', '拥有15年心理咨询经验。', '焦虑症,抑郁症,人际关系', 1, 'admin-001', 'admin-001'),
       ('c-002', '李律师', '企业法律顾问', '10年企业法律顾问经验。', '合同法,劳动法,公司治理', 2, 'admin-001', 'admin-001'),
       ('c-003', '王教练', '职业规划导师', '帮助超过500人实现职业转型。', '职业规划,简历优化', 3, 'admin-001', 'admin-001');

-- Seed branches
insert into branch (id, name, address, phone, status, created_by, updated_by)
values ('b-001', '朝阳咨询中心', '北京市朝阳区建国路88号', '010-88880001', 'ACTIVE', 'admin-001', 'admin-001'),
       ('b-002', '海淀咨询中心', '北京市海淀区中关村大街66号', '010-88880002', 'ACTIVE', 'admin-001', 'admin-001'),
       ('b-003', '浦东咨询中心', '上海市浦东新区陆家嘴路100号', '021-68880003', 'ACTIVE', 'admin-001', 'admin-001');
