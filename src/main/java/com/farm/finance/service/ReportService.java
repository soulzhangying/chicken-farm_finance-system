package com.farm.finance.service;

import com.farm.finance.dto.BatchProfitDTO;
import com.farm.finance.dto.BusinessReportDTO;
import com.farm.finance.dto.DashboardOverviewDTO;
import com.farm.finance.dto.InventoryAlertDTO;
import com.farm.finance.entity.*;
import com.farm.finance.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportService {

    private final FinanceRecordRepository financeRecordRepository;
    private final ChickenBatchRepository chickenBatchRepository;
    private final ChickenSaleRepository chickenSaleRepository;
    private final EggSaleRepository eggSaleRepository;
    private final DeathRecordRepository deathRecordRepository;
    private final FeedRecordRepository feedRecordRepository;
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    /**
     * 生成经营报表
     */
    public BusinessReportDTO generateBusinessReport(LocalDate startDate, LocalDate endDate) {
        BusinessReportDTO report = new BusinessReportDTO();
        report.setStartDate(startDate);
        report.setEndDate(endDate);

        // 获取财务记录
        List<FinanceRecord> records = financeRecordRepository.findByRecordDateBetween(startDate, endDate);

        // 统计收入
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal chickenSaleIncome = BigDecimal.ZERO;
        BigDecimal eggSaleIncome = BigDecimal.ZERO;
        BigDecimal otherIncome = BigDecimal.ZERO;

        // 统计支出
        BigDecimal totalExpense = BigDecimal.ZERO;
        BigDecimal feedExpense = BigDecimal.ZERO;
        BigDecimal chickenExpense = BigDecimal.ZERO;
        BigDecimal laborExpense = BigDecimal.ZERO;
        BigDecimal otherExpense = BigDecimal.ZERO;

        for (FinanceRecord record : records) {
            if (!Boolean.TRUE.equals(record.getIsActive())) continue;

            if ("INCOME".equals(record.getMoneyType())) {
                totalIncome = totalIncome.add(record.getAmount());
                if ("CHICKEN_SALE".equals(record.getIncomeType())) {
                    chickenSaleIncome = chickenSaleIncome.add(record.getAmount());
                } else if ("EGG_SALE".equals(record.getIncomeType())) {
                    eggSaleIncome = eggSaleIncome.add(record.getAmount());
                } else {
                    otherIncome = otherIncome.add(record.getAmount());
                }
            } else if ("EXPENSE".equals(record.getMoneyType()) || "COST".equals(record.getMoneyType())) {
                totalExpense = totalExpense.add(record.getAmount());
                if ("FEED".equals(record.getCostType())) {
                    feedExpense = feedExpense.add(record.getAmount());
                } else if ("CHICKEN".equals(record.getCostType())) {
                    chickenExpense = chickenExpense.add(record.getAmount());
                } else if ("LABOR".equals(record.getCostType())) {
                    laborExpense = laborExpense.add(record.getAmount());
                } else {
                    otherExpense = otherExpense.add(record.getAmount());
                }
            }
        }

        report.setTotalIncome(totalIncome);
        report.setChickenSaleIncome(chickenSaleIncome);
        report.setEggSaleIncome(eggSaleIncome);
        report.setOtherIncome(otherIncome);

        report.setTotalExpense(totalExpense);
        report.setFeedExpense(feedExpense);
        report.setChickenExpense(chickenExpense);
        report.setLaborExpense(laborExpense);
        report.setOtherExpense(otherExpense);

        report.setGrossProfit(totalIncome.subtract(totalExpense));

        // 统计销售数量
        List<ChickenSale> chickenSales = chickenSaleRepository.findBySaleDateBetween(startDate, endDate);
        int chickenCount = 0;
        BigDecimal chickenWeight = BigDecimal.ZERO;
        for (ChickenSale sale : chickenSales) {
            if (Boolean.TRUE.equals(sale.getIsActive())) {
                if (sale.getQuantity() != null) chickenCount += sale.getQuantity();
                if (sale.getWeight() != null) chickenWeight = chickenWeight.add(sale.getWeight());
            }
        }
        report.setChickenSaleCount(chickenCount);
        report.setChickenSaleWeight(chickenWeight);

        List<EggSale> eggSales = eggSaleRepository.findBySaleDateBetween(startDate, endDate);
        int eggCount = 0;
        BigDecimal eggWeight = BigDecimal.ZERO;
        for (EggSale sale : eggSales) {
            if (Boolean.TRUE.equals(sale.getIsActive())) {
                if (sale.getSaleQuantity() != null) eggCount += sale.getSaleQuantity();
                if (sale.getSaleWeight() != null) eggWeight = eggWeight.add(sale.getSaleWeight());
            }
        }
        report.setEggSaleCount(eggCount);
        report.setEggSaleWeight(eggWeight);

        return report;
    }

    /**
     * 批次利润分析
     */
    public BatchProfitDTO analyzeBatchProfit(Long batchId) {
        Optional<ChickenBatch> batchOpt = chickenBatchRepository.findById(batchId);
        if (batchOpt.isEmpty()) {
            return null;
        }

        ChickenBatch batch = batchOpt.get();
        BatchProfitDTO dto = new BatchProfitDTO();

        dto.setBatchId(batchId);
        dto.setBatchNo(batch.getBatchNo());
        dto.setGroupName(batch.getGroupName());
        dto.setEntryDate(batch.getEntryDate());
        dto.setSaleDate(batch.getActualSaleDate());
        dto.setEntryQuantity(batch.getEntryQuantity());
        dto.setCurrentAge(batch.getCurrentAge());

        // 死亡数量
        Integer deathCount = 0;
        List<DeathRecord> deathRecords = deathRecordRepository.findByBatchId(batchId);
        for (DeathRecord record : deathRecords) {
            if (Boolean.TRUE.equals(record.getIsActive())) {
                deathCount += record.getDeathCount();
            }
        }
        dto.setDeathCount(deathCount);

        // 销售数量
        Integer saleQuantity = 0;
        BigDecimal totalIncome = BigDecimal.ZERO;
        List<ChickenSale> sales = chickenSaleRepository.findByBatchId(batchId);
        for (ChickenSale sale : sales) {
            if (Boolean.TRUE.equals(sale.getIsActive())) {
                if (sale.getQuantity() != null) saleQuantity += sale.getQuantity();
                if (sale.getTotalAmount() != null) totalIncome = totalIncome.add(sale.getTotalAmount());
            }
        }
        dto.setSaleQuantity(saleQuantity);
        dto.setTotalIncome(totalIncome);

        // 剩余数量
        dto.setRemainingQuantity(batch.getCurrentQuantity());

        // 进雏成本
        BigDecimal entryCost = batch.getEntryTotalCost() != null ? batch.getEntryTotalCost() : BigDecimal.ZERO;
        dto.setEntryCost(entryCost);

        // 饲料成本
        BigDecimal feedCost = feedRecordRepository.sumCostByBatchId(batchId);
        if (feedCost == null) feedCost = BigDecimal.ZERO;
        dto.setFeedCost(feedCost);

        // 其他成本（暂定为0，可后续扩展）
        BigDecimal otherCost = BigDecimal.ZERO;
        dto.setOtherCost(otherCost);

        // 总成本
        BigDecimal totalCost = entryCost.add(feedCost).add(otherCost);
        dto.setTotalCost(totalCost);

        // 单只成本
        Integer totalChicken = batch.getEntryQuantity();
        if (totalChicken != null && totalChicken > 0) {
            dto.setUnitCost(totalCost.divide(BigDecimal.valueOf(totalChicken), 2, RoundingMode.HALF_UP));
        }

        // 单只收入
        if (saleQuantity != null && saleQuantity > 0) {
            dto.setUnitIncome(totalIncome.divide(BigDecimal.valueOf(saleQuantity), 2, RoundingMode.HALF_UP));
        }

        // 毛利润
        BigDecimal grossProfit = totalIncome.subtract(totalCost);
        dto.setGrossProfit(grossProfit);

        // 单只利润
        if (saleQuantity != null && saleQuantity > 0) {
            dto.setUnitProfit(grossProfit.divide(BigDecimal.valueOf(saleQuantity), 2, RoundingMode.HALF_UP));
        }

        // 利润率
        if (totalCost.compareTo(BigDecimal.ZERO) > 0) {
            dto.setProfitRate(grossProfit.divide(totalCost, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100)));
        }

        // 日均利润
        if (batch.getCurrentAge() != null && batch.getCurrentAge() > 0) {
            dto.setAvgDailyProfit(grossProfit.divide(BigDecimal.valueOf(batch.getCurrentAge()), 2, RoundingMode.HALF_UP));
        }

        return dto;
    }

    /**
     * 获取所有活跃批次的利润分析
     */
    public List<BatchProfitDTO> getAllBatchProfits() {
        List<ChickenBatch> batches = chickenBatchRepository.findByIsActiveTrue();
        List<BatchProfitDTO> results = new ArrayList<>();
        
        for (ChickenBatch batch : batches) {
            BatchProfitDTO dto = analyzeBatchProfit(batch.getId());
            if (dto != null) {
                results.add(dto);
            }
        }
        
        return results;
    }

    /**
     * 库存预警检查
     */
    public List<InventoryAlertDTO> checkInventoryAlerts(BigDecimal minThreshold) {
        List<InventoryAlertDTO> alerts = new ArrayList<>();
        List<Inventory> inventories = inventoryRepository.findByStatus(true);
        
        for (Inventory inv : inventories) {
            if (inv.getQuantity().compareTo(minThreshold) < 0) {
                Optional<Product> productOpt = productRepository.findById(inv.getProductId());
                if (productOpt.isPresent()) {
                    Product product = productOpt.get();
                    
                    InventoryAlertDTO alert = new InventoryAlertDTO();
                    alert.setProductId(product.getId());
                    alert.setProductNo(product.getProductNo());
                    alert.setProductName(product.getName());
                    alert.setProductType(product.getProductType());
                    alert.setUnit(product.getUnit());
                    alert.setCurrentQuantity(inv.getQuantity());
                    alert.setMinQuantity(minThreshold);
                    
                    if (inv.getQuantity().compareTo(minThreshold.multiply(new BigDecimal("0.5"))) < 0) {
                        alert.setAlertLevel("CRITICAL");
                        alert.setAlertMessage("库存告急！当前库存仅剩 " + inv.getQuantity() + product.getUnit());
                    } else {
                        alert.setAlertLevel("LOW");
                        alert.setAlertMessage("库存不足，当前库存 " + inv.getQuantity() + product.getUnit());
                    }
                    
                    alerts.add(alert);
                }
            }
        }
        
        return alerts;
    }

    /**
     * 获取仪表盘概览数据
     */
    public DashboardOverviewDTO getDashboardOverview() {
        DashboardOverviewDTO dashboard = new DashboardOverviewDTO();
        
        // 批次统计
        dashboard.setActiveBatchCount(chickenBatchRepository.countByStatus("ACTIVE"));
        
        // 今日收入支出
        LocalDate today = LocalDate.now();
        dashboard.setTodayIncome(financeRecordRepository.sumIncomeByDateRange(today, today));
        dashboard.setTodayExpense(financeRecordRepository.sumCostByDateRange(today, today));
        
        // 本月收入支出
        LocalDate monthStart = today.withDayOfMonth(1);
        dashboard.setMonthIncome(financeRecordRepository.sumIncomeByDateRange(monthStart, today));
        dashboard.setMonthExpense(financeRecordRepository.sumCostByDateRange(monthStart, today));
        
        // 总存栏
        List<ChickenBatch> activeBatches = chickenBatchRepository.findByStatus("ACTIVE");
        int totalStock = 0;
        for (ChickenBatch batch : activeBatches) {
            totalStock += batch.getCurrentQuantity();
        }
        dashboard.setTotalStock(totalStock);
        
        return dashboard;
    }
}
