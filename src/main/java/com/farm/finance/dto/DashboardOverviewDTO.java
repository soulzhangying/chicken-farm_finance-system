package com.farm.finance.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 仪表盘概览DTO
 */
@Data
public class DashboardOverviewDTO {
    private Long activeBatchCount;       // 活跃批次数
    private Integer totalStock;          // 总存栏数
    private BigDecimal todayIncome;      // 今日收入
    private BigDecimal todayExpense;     // 今日支出
    private BigDecimal monthIncome;      // 本月收入
    private BigDecimal monthExpense;     // 本月支出
}
