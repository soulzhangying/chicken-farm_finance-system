mysqldump : mysqldump: [Warning] Using a password on the command line interface can be insecure.
At line:1 char:1
+ mysqldump -u root -pRoot@123456 -h localhost -P 3306 farm_finance --n ...
+ ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    + CategoryInfo          : NotSpecified: (mysqldump: [War...an be insecure.:String) [], RemoteException
    + FullyQualifiedErrorId : NativeCommandError
 
-- MySQL dump 10.13  Distrib 8.0.44, for Win64 (x86_64)
--
-- Host: localhost    Database: farm_finance
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
-- Table structure for table `chicken_change`
--

DROP TABLE IF EXISTS `chicken_change`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chicken_change` (
  `id` int NOT NULL,
  `change_date` date NOT NULL,
  `group_id` int NOT NULL,
  `change_tpye` varchar(10) NOT NULL,
  `count` int NOT NULL,
  `price_type` varchar(10) DEFAULT NULL,
  `unit_price` decimal(6,2) DEFAULT NULL,
  `quantity` int NOT NULL,
  `total_money` decimal(10,2) DEFAULT NULL,
  `egg_price_type` varchar(10) DEFAULT NULL,
  `egg_quantity` int NOT NULL,
  `egg_unit_price` decimal(6,2) DEFAULT NULL,
  `egg_total_amount` decimal(8,2) DEFAULT NULL,
  `reason` varchar(50) DEFAULT NULL,
  `related_money_id` int DEFAULT NULL,
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  KEY `idx_group_date` (`group_id`,`change_date`) /*!80000 INVISIBLE */,
  KEY `idx_type` (`change_tpye`) /*!80000 INVISIBLE */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chicken_group`
--

DROP TABLE IF EXISTS `chicken_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `chicken_group` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '鸡群编号',
  `group_name` varchar(20) NOT NULL COMMENT '批次名称',
  `chicken_type` varchar(10) NOT NULL,
  `start_date` date NOT NULL,
  `start_count` int NOT NULL,
  `current_count` int NOT NULL,
  `avg_cost` decimal(8,2) DEFAULT '0.00' COMMENT '平均成本',
  `total_cost` decimal(12,2) DEFAULT '0.00' COMMENT '总成本',
  `status` varchar(10) DEFAULT '养殖中',
  `notes` varchar(200) DEFAULT '备注',
  `created_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `farm_user`
--

DROP TABLE IF EXISTS `farm_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `farm_user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(20) NOT NULL,
  `real_name` varchar(10) DEFAULT NULL,
  `role` varchar(11) NOT NULL,
  `phone` varchar(11) DEFAULT NULL,
  `wechat` varchar(50) DEFAULT NULL,
  `password` varchar(100) NOT NULL DEFAULT '123456',
  `is_active` tinyint DEFAULT '1',
  `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username_UNIQUE` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
-- Table structure for table `money_record`
--

DROP TABLE IF EXISTS `money_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `money_record` (
  `id` int NOT NULL,
  `record_date` date DEFAULT NULL,
  `money_type` varchar(10) NOT NULL COMMENT '资金类型：收入/支出',
  `income_type` varchar(20) DEFAULT NULL COMMENT '收入类型：商家买鸡/散客买鸡/卖鸡蛋/其他收入',
  `cost_type` varchar(20) DEFAULT NULL COMMENT '支出类型：买饲料/买药/发工资/水电费/其他支出',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `pay_way` varchar(10) DEFAULT '微信',
  `customer_id` int DEFAULT NULL,
  `supplier_name` varchar(50) DEFAULT NULL COMMENT '供应商',
  `chicken_change_id` int DEFAULT NULL COMMENT '关联的鸡只变动记录',
  `proof_image` varchar(200) DEFAULT NULL COMMENT '凭证图片路径',
  `created_by` int NOT NULL COMMENT '创建人',
  `created_time` timestamp NULL DEFAULT NULL,
  KEY `idx_date` (`record_date`) /*!80000 INVISIBLE */,
  KEY `idx_type` (`money_type`) /*!80000 INVISIBLE */,
  KEY `idx_customer` (`customer_id`) /*!80000 INVISIBLE */
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

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
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-23 12:27:05
