package com.farm.finance.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 批次利润分析DTO
 */
@Data
public class BatchProfitDTO {
    private Long batchId;
    private String batchNo;
    private String groupName;
    private LocalDate entryDate;
    private LocalDate saleDate;
    
    // 数量统计
    private Integer entryQuantity;          // 进雏数量
    private Integer deathCount;             // 死亡数量
    private Integer saleQuantity;           // 销售数量
    private Integer remainingQuantity;      // 剩余数量
    
    // 成本统计
    private BigDecimal entryCost;           // 进雏成本
    private BigDecimal feedCost;            // 饲料成本
    private BigDecimal otherCost;           // 其他成本
    private BigDecimal totalCost;           // 总成本
    private BigDecimal unitCost;            // 单只成本
    
    // 收入统计
    private BigDecimal totalIncome;         // 总收入
    private BigDecimal unitIncome;          // 单只收入
    
    // 利润统计
    private BigDecimal grossProfit;         // 毛利润
    private BigDecimal unitProfit;          // 单只利润
    private BigDecimal profitRate;          // 利润率(%)
    
    // 日龄
    private Integer currentAge;             // 当前日龄
    private BigDecimal avgDailyProfit;      // 日均利润
}
