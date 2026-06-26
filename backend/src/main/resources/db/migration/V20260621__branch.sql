CREATE TABLE IF NOT EXISTS branch (
    id VARCHAR(64) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    address VARCHAR(255),
    phone VARCHAR(20),
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_by VARCHAR(64),
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by VARCHAR(64),
    updated_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO branch (id, name, address, phone, status) VALUES
('b-001', '朝阳咨询中心', '北京市朝阳区建国路88号', '010-88880001', 'ACTIVE'),
('b-002', '海淀咨询中心', '北京市海淀区中关村大街66号', '010-88880002', 'ACTIVE'),
('b-003', '浦东咨询中心', '上海市浦东新区陆家嘴路100号', '021-68880003', 'ACTIVE')
ON DUPLICATE KEY UPDATE name=VALUES(name);
