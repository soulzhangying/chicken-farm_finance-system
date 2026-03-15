DELETE FROM sys_user WHERE username = 'admin';
INSERT INTO sys_user (username, password, real_name, role, is_active, created_time) 
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKyAVvVcW.Ld5nB.0P5GqC4yU1A2', '系统管理员', 'ADMIN', 1, NOW());
