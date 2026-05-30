package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.dto.BatchProfitDTO;
import com.farm.finance.dto.BusinessReportDTO;
import com.farm.finance.dto.DashboardOverviewDTO;
import com.farm.finance.dto.InventoryAlertDTO;
import com.farm.finance.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    /**
     * 获取仪表盘概览
     */
    @GetMapping("/dashboard")
    public Result<DashboardOverviewDTO> getDashboardOverview() {
        return Result.success(reportService.getDashboardOverview());
    }

    /**
     * 生成经营报表
     */
    @GetMapping("/business")
    public Result<BusinessReportDTO> generateBusinessReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(reportService.generateBusinessReport(startDate, endDate));
    }

    /**
     * 鸡舍利润分析
     */
    @GetMapping("/house-profit/{houseId}")
    public Result<BatchProfitDTO> analyzeHouseProfit(@PathVariable Long houseId) {
        BatchProfitDTO result = reportService.analyzeBatchProfit(houseId);
        if (result == null) {
            return Result.error(404, "鸡舍不存在");
        }
        return Result.success(result);
    }

    /**
     * 所有鸡舍利润分析
     */
    @GetMapping("/house-profits")
    public Result<List<BatchProfitDTO>> getAllHouseProfits() {
        return Result.success(reportService.getAllBatchProfits());
    }

    /**
     * 库存预警检查
     */
    @GetMapping("/inventory-alerts")
    public Result<List<InventoryAlertDTO>> checkInventoryAlerts(
            @RequestParam(defaultValue = "10") BigDecimal minThreshold) {
        return Result.success(reportService.checkInventoryAlerts(minThreshold));
    }
}