-- 密码: 123456
DELETE FROM sys_user WHERE username = 'admin';
INSERT INTO sys_user (username, password, real_name, role, is_active, created_time) VALUES ('admin', '$2a$10$EqKcp1WFKVQISheBxmXNGexPR.i7QYXOJC.OFfQDT8iSaHuuPdlrW', 'Admin', 'ADMIN', 1, NOW());
SELECT id, username, real_name, role, is_active FROM sys_user WHERE username = 'admin';
