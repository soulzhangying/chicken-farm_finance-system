package com.farm.finance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 经营报表统计DTO
 */
@Data
public class BusinessReportDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    
    // 收入统计
    private BigDecimal totalIncome;         // 总收入
    private BigDecimal chickenSaleIncome;   // 肉鸡销售收入
    private BigDecimal eggSaleIncome;       // 鸡蛋销售收入
    private BigDecimal otherIncome;         // 其他收入
    
    // 支出统计
    private BigDecimal totalExpense;        // 总支出
    private BigDecimal feedExpense;         // 饲料支出
    private BigDecimal chickenExpense;      // 鸡苗支出
    private BigDecimal laborExpense;        // 人工支出
    private BigDecimal otherExpense;        // 其他支出
    
    // 利润统计
    private BigDecimal grossProfit;         // 毛利润
    
    // 销售统计
    private Integer chickenSaleCount;       // 肉鸡销售数量
    private BigDecimal chickenSaleWeight;   // 肉鸡销售重量
    private Integer eggSaleCount;           // 鸡蛋销售数量
    private BigDecimal eggSaleWeight;       // 鸡蛋销售重量
}
