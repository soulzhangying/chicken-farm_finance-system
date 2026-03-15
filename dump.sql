-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: farm_finance
-- ------------------------------------------------------
-- Server version	8.0.44

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `chicken_batch`
--

DROP TABLE IF EXISTS `chicken_batch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chicken_batch` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '批次ID',
  `batch_no` varchar(20) NOT NULL COMMENT '批次编号',
  `house_id` bigint NOT NULL COMMENT '鸡舍ID',
  `group_name` varchar(50) NOT NULL COMMENT '鸡群名称',
  `breed` varchar(50) DEFAULT NULL COMMENT '品种',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID(鸡苗来源)',
  `entry_date` date NOT NULL COMMENT '进雏日期',
  `entry_quantity` int NOT NULL COMMENT '进雏数量',
  `entry_price` decimal(6,2) DEFAULT NULL COMMENT '进雏单价(元/只)',
  `entry_total_cost` decimal(10,2) DEFAULT NULL COMMENT '进雏总成本',
  `current_quantity` int NOT NULL COMMENT '当前存栏数量',
  `current_age` int NOT NULL DEFAULT '0' COMMENT '当前日龄(天)',
  `current_weight` decimal(8,2) DEFAULT NULL COMMENT '平均体重(克)',
  `expected_sale_date` date DEFAULT NULL COMMENT '预计出栏日期',
  `actual_sale_date` date DEFAULT NULL COMMENT '实际出栏日期',
  `total_feed_cost` decimal(12,2) NOT NULL DEFAULT '0.00' COMMENT '总饲料成本',
  `total_death_count` int NOT NULL DEFAULT '0' COMMENT '总死亡数量',
  `total_egg_count` int NOT NULL DEFAULT '0' COMMENT '总产蛋数量(副产品)',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE-养殖中, COMPLETED-已出栏',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_batch_no` (`batch_no`),
  KEY `idx_house_id` (`house_id`),
  KEY `idx_entry_date` (`entry_date`),
  KEY `idx_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='肉鸡批次表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chicken_batch`
--

LOCK TABLES `chicken_batch` WRITE;
/*!40000 ALTER TABLE `chicken_batch` DISABLE KEYS */;
INSERT INTO `chicken_batch` VALUES (1,'B202501001',1,'第一批肉鸡','白羽肉鸡',2,'2025-01-01',500,5.00,2500.00,495,42,NULL,NULL,NULL,0.00,0,0,'ACTIVE',NULL,'2026-02-17 06:57:28');
/*!40000 ALTER TABLE `chicken_batch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chicken_house`
--

DROP TABLE IF EXISTS `chicken_house`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chicken_house` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '鸡舍ID',
  `house_no` varchar(20) NOT NULL COMMENT '鸡舍编号',
  `name` varchar(50) NOT NULL COMMENT '鸡舍名称',
  `capacity` int NOT NULL COMMENT '设计容量',
  `area` decimal(10,2) DEFAULT NULL COMMENT '面积(平方米)',
  `status` varchar(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态: ACTIVE-使用中, EMPTY-空闲',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_house_no` (`house_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='鸡舍表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chicken_house`
--

LOCK TABLES `chicken_house` WRITE;
/*!40000 ALTER TABLE `chicken_house` DISABLE KEYS */;
INSERT INTO `chicken_house` VALUES (1,'H001','1号鸡舍',2000,500.00,'ACTIVE','2026-02-17 06:57:28'),(2,'H002','2号鸡舍',2000,500.00,'ACTIVE','2026-02-17 06:57:28');
/*!40000 ALTER TABLE `chicken_house` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `chicken_sale`
--

DROP TABLE IF EXISTS `chicken_sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chicken_sale` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '出栏ID',
  `sale_no` varchar(20) NOT NULL COMMENT '出栏单号',
  `batch_id` bigint NOT NULL COMMENT '批次ID',
  `sale_date` date NOT NULL COMMENT '出栏日期',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `sale_quantity` int NOT NULL COMMENT '出栏数量',
  `sale_weight` decimal(10,2) DEFAULT NULL COMMENT '总重量(斤)',
  `unit_price` decimal(6,2) DEFAULT NULL COMMENT '单价(元/斤)',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `customer_id` bigint DEFAULT NULL COMMENT '客户ID',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `transport_cost` decimal(8,2) DEFAULT '0.00' COMMENT '运输成本',
  `profit` decimal(10,2) DEFAULT NULL COMMENT '利润',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sale_no` (`sale_no`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_sale_date` (`sale_date`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='肉鸡出栏记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `chicken_sale`
--

LOCK TABLES `chicken_sale` WRITE;
/*!40000 ALTER TABLE `chicken_sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `chicken_sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '客户ID',
  `customer_no` varchar(20) NOT NULL COMMENT '客户编号',
  `name` varchar(100) NOT NULL COMMENT '客户名称',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_no` (`customer_no`),
  KEY `idx_name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='客户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'C001','张三','13800138001','XX市XX区',1,'2026-02-17 06:57:28'),(2,'C002','李四超市','13800138002','XX市XX区',1,'2026-02-17 06:57:28');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `death_record`
--

DROP TABLE IF EXISTS `death_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `death_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '死亡记录ID',
  `batch_id` bigint NOT NULL COMMENT '批次ID',
  `death_date` date NOT NULL COMMENT '死亡日期',
  `death_count` int NOT NULL COMMENT '死亡数量',
  `death_reason` varchar(100) DEFAULT NULL COMMENT '死亡原因',
  `operator_id` bigint NOT NULL COMMENT '操作员ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_death_date` (`death_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='死亡记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `death_record`
--

LOCK TABLES `death_record` WRITE;
/*!40000 ALTER TABLE `death_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `death_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `egg_record`
--

DROP TABLE IF EXISTS `egg_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `egg_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产蛋记录ID',
  `batch_id` bigint NOT NULL COMMENT '批次ID',
  `production_date` date NOT NULL COMMENT '产蛋日期',
  `product_id` bigint NOT NULL COMMENT '鸡蛋产品ID',
  `total_count` int NOT NULL COMMENT '总产蛋数量',
  `total_weight` decimal(10,2) DEFAULT NULL COMMENT '总重量(斤)',
  `operator_id` bigint NOT NULL COMMENT '操作员ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_production_date` (`production_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产蛋记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `egg_record`
--

LOCK TABLES `egg_record` WRITE;
/*!40000 ALTER TABLE `egg_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `egg_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `egg_sale`
--

DROP TABLE IF EXISTS `egg_sale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `egg_sale` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '销售ID',
  `sale_no` varchar(20) NOT NULL COMMENT '销售单号',
  `sale_date` date NOT NULL COMMENT '销售日期',
  `product_id` bigint NOT NULL COMMENT '鸡蛋产品ID',
  `sale_quantity` int NOT NULL COMMENT '销售数量',
  `sale_weight` decimal(10,2) DEFAULT NULL COMMENT '总重量(斤)',
  `unit_price` decimal(6,2) DEFAULT NULL COMMENT '单价(元/斤)',
  `total_amount` decimal(10,2) NOT NULL COMMENT '总金额',
  `customer_id` bigint DEFAULT NULL COMMENT '客户ID',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `remarks` varchar(200) DEFAULT NULL COMMENT '备注',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_sale_no` (`sale_no`),
  KEY `idx_sale_date` (`sale_date`),
  KEY `idx_customer_id` (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='鸡蛋销售记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `egg_sale`
--

LOCK TABLES `egg_sale` WRITE;
/*!40000 ALTER TABLE `egg_sale` DISABLE KEYS */;
/*!40000 ALTER TABLE `egg_sale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `feed_record`
--

DROP TABLE IF EXISTS `feed_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `feed_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '饲料记录ID',
  `batch_id` bigint NOT NULL COMMENT '批次ID',
  `record_date` date NOT NULL COMMENT '投喂日期',
  `product_id` bigint NOT NULL COMMENT '饲料产品ID',
  `quantity` decimal(10,2) NOT NULL COMMENT '投喂数量(斤)',
  `unit_price` decimal(6,2) NOT NULL COMMENT '单价(元/斤)',
  `total_cost` decimal(10,2) NOT NULL COMMENT '饲料成本',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID',
  `operator_id` bigint NOT NULL COMMENT '操作员ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_batch_id` (`batch_id`),
  KEY `idx_record_date` (`record_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='饲料记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `feed_record`
--

LOCK TABLES `feed_record` WRITE;
/*!40000 ALTER TABLE `feed_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `feed_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finance_record`
--

DROP TABLE IF EXISTS `finance_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finance_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `record_no` varchar(20) NOT NULL COMMENT '记录编号',
  `record_date` date NOT NULL COMMENT '记录日期',
  `money_type` varchar(20) NOT NULL COMMENT '资金类型: INCOME-收入, EXPENSE-支出',
  `amount` decimal(12,2) NOT NULL COMMENT '金额',
  `cost_type` varchar(20) DEFAULT NULL COMMENT '支出类型: FEED-饲料, CHICKEN-鸡苗, LABOR-人工, OTHER-其他',
  `income_type` varchar(20) DEFAULT NULL COMMENT '收入类型: CHICKEN_SALE-卖鸡, EGG_SALE-卖蛋, OTHER-其他',
  `product_id` bigint DEFAULT NULL COMMENT '关联产品ID',
  `batch_no` varchar(20) DEFAULT NULL COMMENT '关联批次号',
  `customer_id` bigint DEFAULT NULL COMMENT '客户ID(收入时关联)',
  `supplier_id` bigint DEFAULT NULL COMMENT '供应商ID(支出时关联)',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式',
  `reference_id` varchar(50) DEFAULT NULL COMMENT '关联单据ID',
  `reference_type` varchar(20) DEFAULT NULL COMMENT '关联单据类型',
  `description` varchar(200) DEFAULT NULL COMMENT '描述',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_record_no` (`record_no`),
  KEY `idx_record_date` (`record_date`),
  KEY `idx_money_type` (`money_type`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_batch_no` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='财务收支记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finance_record`
--

LOCK TABLES `finance_record` WRITE;
/*!40000 ALTER TABLE `finance_record` DISABLE KEYS */;
/*!40000 ALTER TABLE `finance_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '库存ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `batch_no` varchar(20) DEFAULT NULL COMMENT '关联批次号(肉鸡、鸡蛋)',
  `quantity` decimal(10,2) NOT NULL COMMENT '库存数量',
  `unit` varchar(10) NOT NULL COMMENT '单位',
  `location` varchar(50) DEFAULT NULL COMMENT '存放位置',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态: 0-已冻结, 1-正常',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_batch_no` (`batch_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory_transaction`
--

DROP TABLE IF EXISTS `inventory_transaction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inventory_transaction` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '变动ID',
  `transaction_no` varchar(20) NOT NULL COMMENT '变动编号',
  `transaction_date` date NOT NULL COMMENT '变动日期',
  `transaction_type` varchar(20) NOT NULL COMMENT '变动类型: IN-入库, OUT-出库, ADJUST-调整',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `batch_no` varchar(20) DEFAULT NULL COMMENT '批次号',
  `quantity` decimal(10,2) NOT NULL COMMENT '变动数量(正数为入库，负数为出库)',
  `unit_price` decimal(6,2) DEFAULT NULL COMMENT '单价',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '总金额',
  `price_type` varchar(20) DEFAULT NULL COMMENT '价格类型: RETAIL-零售, WHOLESALE-批发, COST-成本',
  `before_quantity` decimal(10,2) NOT NULL COMMENT '变动前数量',
  `after_quantity` decimal(10,2) NOT NULL COMMENT '变动后数量',
  `reference_id` varchar(50) DEFAULT NULL COMMENT '关联单据ID',
  `reference_type` varchar(20) DEFAULT NULL COMMENT '关联单据类型',
  `related_money_id` bigint DEFAULT NULL COMMENT '关联财务记录ID',
  `reason` varchar(50) DEFAULT NULL COMMENT '变动原因',
  `operator_id` bigint NOT NULL COMMENT '操作员ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_transaction_no` (`transaction_no`),
  KEY `idx_product_id` (`product_id`),
  KEY `idx_batch_no` (`batch_no`),
  KEY `idx_transaction_date` (`transaction_date`),
  KEY `idx_related_money_id` (`related_money_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存变动记录表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory_transaction`
--

LOCK TABLES `inventory_transaction` WRITE;
/*!40000 ALTER TABLE `inventory_transaction` DISABLE KEYS */;
/*!40000 ALTER TABLE `inventory_transaction` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product`
--

DROP TABLE IF EXISTS `product`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '产品ID',
  `product_no` varchar(20) NOT NULL COMMENT '产品编号',
  `name` varchar(100) NOT NULL COMMENT '产品名称',
  `product_type` varchar(20) NOT NULL COMMENT '产品类型: CHICKEN-肉鸡, EGG-鸡蛋, FEED-饲料',
  `unit` varchar(10) NOT NULL COMMENT '单位:只,斤,个,包',
  `purchase_price` decimal(10,2) DEFAULT NULL COMMENT '采购成本价',
  `sale_price` decimal(10,2) DEFAULT NULL COMMENT '销售价',
  `wholesale_price` decimal(10,2) DEFAULT NULL COMMENT '批发价',
  `description` varchar(500) DEFAULT NULL COMMENT '产品描述',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-下架,1-上架',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_no` (`product_no`),
  KEY `idx_product_type` (`product_type`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='产品表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product`
--

LOCK TABLES `product` WRITE;
/*!40000 ALTER TABLE `product` DISABLE KEYS */;
INSERT INTO `product` VALUES (1,'P001','新鲜鸡蛋','EGG','斤',NULL,8.00,6.50,NULL,1,'2026-02-17 06:57:28'),(2,'P002','土鸡蛋','EGG','斤',NULL,12.00,10.00,NULL,1,'2026-02-17 06:57:28'),(3,'P003','肉鸡饲料','FEED','包',100.00,NULL,NULL,NULL,1,'2026-02-17 06:57:28'),(4,'P004','肉鸡','CHICKEN','只',5.00,45.00,38.00,NULL,1,'2026-02-17 06:57:28');
/*!40000 ALTER TABLE `product` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order`
--

DROP TABLE IF EXISTS `purchase_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '采购订单ID',
  `order_no` varchar(20) NOT NULL COMMENT '采购单号',
  `supplier_id` bigint NOT NULL COMMENT '供应商ID',
  `order_date` date NOT NULL COMMENT '采购日期',
  `total_amount` decimal(12,2) NOT NULL COMMENT '订单总金额',
  `payment_status` varchar(20) NOT NULL DEFAULT 'UNPAID' COMMENT '付款状态: UNPAID-未付款, PAID-已付款',
  `delivery_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '收货状态: PENDING-待收货, COMPLETED-已完成',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_by` bigint NOT NULL COMMENT '创建人ID',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_supplier_id` (`supplier_id`),
  KEY `idx_order_date` (`order_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order`
--

LOCK TABLES `purchase_order` WRITE;
/*!40000 ALTER TABLE `purchase_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `purchase_order_item`
--

DROP TABLE IF EXISTS `purchase_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `purchase_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '采购订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `quantity` decimal(10,2) NOT NULL COMMENT '采购数量',
  `unit` varchar(10) NOT NULL COMMENT '单位',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '小计',
  `received_quantity` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '已收货数量',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='采购订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `purchase_order_item`
--

LOCK TABLES `purchase_order_item` WRITE;
/*!40000 ALTER TABLE `purchase_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `purchase_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order`
--

DROP TABLE IF EXISTS `sales_order`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_order` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(20) NOT NULL COMMENT '订单号',
  `customer_id` bigint NOT NULL COMMENT '客户ID',
  `order_date` datetime NOT NULL COMMENT '下单时间',
  `total_amount` decimal(12,2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '优惠金额',
  `actual_amount` decimal(12,2) NOT NULL COMMENT '实付金额',
  `payment_status` varchar(20) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态: UNPAID-未支付, PAID-已支付',
  `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式: CASH-现金, WECHAT-微信, ALIPAY-支付宝, CREDIT-赊账',
  `order_status` varchar(20) NOT NULL DEFAULT 'PENDING' COMMENT '订单状态: PENDING-待处理, COMPLETED-已完成, CANCELLED-已取消',
  `delivery_type` varchar(20) NOT NULL COMMENT '配送方式: PICKUP-自提, DELIVERY-配送',
  `delivery_address` varchar(200) DEFAULT NULL COMMENT '配送地址',
  `delivery_fee` decimal(10,2) NOT NULL DEFAULT '0.00' COMMENT '配送费',
  `contact_name` varchar(50) DEFAULT NULL COMMENT '联系人',
  `contact_phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `remarks` varchar(500) DEFAULT NULL COMMENT '备注',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_customer_id` (`customer_id`),
  KEY `idx_order_date` (`order_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售订单表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order`
--

LOCK TABLES `sales_order` WRITE;
/*!40000 ALTER TABLE `sales_order` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_order` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_order_item`
--

DROP TABLE IF EXISTS `sales_order_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_order_item` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '明细ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `product_id` bigint NOT NULL COMMENT '产品ID',
  `product_name` varchar(100) NOT NULL COMMENT '产品名称',
  `batch_no` varchar(20) DEFAULT NULL COMMENT '批次号',
  `quantity` decimal(10,2) NOT NULL COMMENT '数量',
  `unit` varchar(10) NOT NULL COMMENT '单位',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '小计',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_product_id` (`product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='销售订单明细表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_order_item`
--

LOCK TABLES `sales_order_item` WRITE;
/*!40000 ALTER TABLE `sales_order_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `sales_order_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `supplier`
--

DROP TABLE IF EXISTS `supplier`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supplier` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '供应商ID',
  `supplier_no` varchar(20) NOT NULL COMMENT '供应商编号',
  `name` varchar(100) NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_no` (`supplier_no`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='供应商表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supplier`
--

LOCK TABLES `supplier` WRITE;
/*!40000 ALTER TABLE `supplier` DISABLE KEYS */;
INSERT INTO `supplier` VALUES (1,'S001','饲料批发商','王五','13900139001',1,'2026-02-17 06:57:28'),(2,'S002','鸡苗供应商','赵六','13900139002',1,'2026-02-17 06:57:28');
/*!40000 ALTER TABLE `supplier` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_operation_log`
--

DROP TABLE IF EXISTS `sys_operation_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operation_log` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志ID',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `operation` varchar(50) NOT NULL COMMENT '操作',
  `ip` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `status` tinyint NOT NULL COMMENT '状态: 0-失败, 1-成功',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_operation_log`
--

LOCK TABLES `sys_operation_log` WRITE;
/*!40000 ALTER TABLE `sys_operation_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_operation_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码(加密)',
  `real_name` varchar(50) NOT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `role` varchar(20) NOT NULL DEFAULT 'STAFF' COMMENT '角色:ADMIN,STAFF',
  `is_active` tinyint NOT NULL DEFAULT '1' COMMENT '状态:0-禁用,1-启用',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
INSERT INTO `sys_user` VALUES (1,'admin','$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH','系统管理员','13800138000','ADMIN',1,'2026-02-17 06:57:28');
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-24 20:20:57
