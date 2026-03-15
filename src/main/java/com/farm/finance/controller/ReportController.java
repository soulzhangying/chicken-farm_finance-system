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
     * 批次利润分析
     */
    @GetMapping("/batch-profit/{batchId}")
    public Result<BatchProfitDTO> analyzeBatchProfit(@PathVariable Long batchId) {
        BatchProfitDTO result = reportService.analyzeBatchProfit(batchId);
        if (result == null) {
            return Result.error(404, "批次不存在");
        }
        return Result.success(result);
    }

    /**
     * 所有批次利润分析
     */
    @GetMapping("/batch-profits")
    public Result<List<BatchProfitDTO>> getAllBatchProfits() {
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
