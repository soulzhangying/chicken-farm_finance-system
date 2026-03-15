-- 软删除迁移脚本
-- 为所有表添加 is_active 字段（如果不存在）
-- 已有 is_active 字段的表：customer, product, supplier, sys_user

-- 1. chicken_batch 表
ALTER TABLE chicken_batch ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 2. chicken_house 表
ALTER TABLE chicken_house ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 3. chicken_sale 表
ALTER TABLE chicken_sale ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 4. death_record 表
ALTER TABLE death_record ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 5. egg_record 表
ALTER TABLE egg_record ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 6. egg_sale 表
ALTER TABLE egg_sale ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 7. feed_record 表
ALTER TABLE feed_record ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 8. finance_record 表
ALTER TABLE finance_record ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 9. inventory 表
ALTER TABLE inventory ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 10. inventory_transaction 表
ALTER TABLE inventory_transaction ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 11. purchase_order 表
ALTER TABLE purchase_order ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 12. purchase_order_item 表
ALTER TABLE purchase_order_item ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 13. sales_order 表
ALTER TABLE sales_order ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 14. sales_order_item 表
ALTER TABLE sales_order_item ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 15. sys_operation_log 表
ALTER TABLE sys_operation_log ADD COLUMN IF NOT EXISTS is_active BOOLEAN NOT NULL DEFAULT TRUE;

-- 验证：查看所有表的 is_active 字段
SELECT TABLE_NAME, COLUMN_NAME, COLUMN_DEFAULT 
FROM INFORMATION_SCHEMA.COLUMNS 
WHERE COLUMN_NAME = 'is_active' 
AND TABLE_SCHEMA = 'farm_finance';
