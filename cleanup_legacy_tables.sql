-- 清理遗留的无用数据库表
-- 这些表是旧版本遗留的，已被新表替代

-- 删除鸡只变动表（已被 chicken_sale, death_record 等替代）
DROP TABLE IF EXISTS chicken_change;

-- 删除鸡群表（已被 chicken_batch 替代）
DROP TABLE IF EXISTS chicken_group;

-- 删除旧版农场用户表（已被 sys_user 替代）
DROP TABLE IF EXISTS farm_user;

-- 删除旧版资金记录表（已被 finance_record 替代）
DROP TABLE IF EXISTS money_record;
