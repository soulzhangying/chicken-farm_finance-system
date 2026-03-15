package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.FinanceRecord;
import com.farm.finance.service.FinanceRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/finance-records")
@RequiredArgsConstructor
public class FinanceRecordController {
    
    private final FinanceRecordService financeRecordService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<FinanceRecord>> findAll() {
        return Result.success(financeRecordService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<FinanceRecord> findById(@PathVariable Long id) {
        return financeRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "财务记录不存在"));
    }
    
    @GetMapping("/record-no/{recordNo}")
    public Result<FinanceRecord> findByRecordNo(@PathVariable String recordNo) {
        return financeRecordService.findByRecordNo(recordNo)
                .map(Result::success)
                .orElse(Result.error(404, "财务记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/money-type/{moneyType}")
    public Result<List<FinanceRecord>> findByMoneyType(@PathVariable String moneyType) {
        return Result.success(financeRecordService.findByMoneyType(moneyType));
    }
    
    @GetMapping("/cost-type/{costType}")
    public Result<List<FinanceRecord>> findByCostType(@PathVariable String costType) {
        return Result.success(financeRecordService.findByCostType(costType));
    }
    
    @GetMapping("/income-type/{incomeType}")
    public Result<List<FinanceRecord>> findByIncomeType(@PathVariable String incomeType) {
        return Result.success(financeRecordService.findByIncomeType(incomeType));
    }
    
    @GetMapping("/product/{productId}")
    public Result<List<FinanceRecord>> findByProductId(@PathVariable Long productId) {
        return Result.success(financeRecordService.findByProductId(productId));
    }
    
    @GetMapping("/batch-no/{batchNo}")
    public Result<List<FinanceRecord>> findByBatchNo(@PathVariable String batchNo) {
        return Result.success(financeRecordService.findByBatchNo(batchNo));
    }
    
    @GetMapping("/customer/{customerId}")
    public Result<List<FinanceRecord>> findByCustomerId(@PathVariable Long customerId) {
        return Result.success(financeRecordService.findByCustomerId(customerId));
    }
    
    @GetMapping("/supplier/{supplierId}")
    public Result<List<FinanceRecord>> findBySupplierId(@PathVariable Long supplierId) {
        return Result.success(financeRecordService.findBySupplierId(supplierId));
    }
    
    @GetMapping("/created-by/{createdBy}")
    public Result<List<FinanceRecord>> findByCreatedBy(@PathVariable Long createdBy) {
        return Result.success(financeRecordService.findByCreatedBy(createdBy));
    }
    
    @GetMapping("/payment-method/{paymentMethod}")
    public Result<List<FinanceRecord>> findByPaymentMethod(@PathVariable String paymentMethod) {
        return Result.success(financeRecordService.findByPaymentMethod(paymentMethod));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<FinanceRecord>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(financeRecordService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<FinanceRecord>> findByIsActiveTrue() {
        return Result.success(financeRecordService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/record-date")
    public Result<List<FinanceRecord>> findByRecordDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(financeRecordService.findByRecordDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<FinanceRecord>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(financeRecordService.findAll(pageable));
    }
    
    @GetMapping("/money-type/{moneyType}/page")
    public Result<Page<FinanceRecord>> findByMoneyTypePage(
            @PathVariable String moneyType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(financeRecordService.findByMoneyType(moneyType, pageable));
    }
    
    @GetMapping("/customer/{customerId}/page")
    public Result<Page<FinanceRecord>> findByCustomerIdPage(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(financeRecordService.findByCustomerId(customerId, pageable));
    }
    
    @GetMapping("/supplier/{supplierId}/page")
    public Result<Page<FinanceRecord>> findBySupplierIdPage(
            @PathVariable Long supplierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(financeRecordService.findBySupplierId(supplierId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<FinanceRecord>> search(
            @RequestParam(required = false) String moneyType,
            @RequestParam(required = false) String costType,
            @RequestParam(required = false) String incomeType,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(financeRecordService.search(moneyType, costType, incomeType, customerId, supplierId, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<FinanceRecord>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(financeRecordService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(financeRecordService.countByIsActiveTrue());
    }
    
    @GetMapping("/sum/income")
    public Result<BigDecimal> sumIncomeByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(financeRecordService.sumIncomeByDateRange(startDate, endDate));
    }
    
    @GetMapping("/sum/cost")
    public Result<BigDecimal> sumCostByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(financeRecordService.sumCostByDateRange(startDate, endDate));
    }
    
    @GetMapping("/total/income")
    public Result<BigDecimal> sumTotalIncome() {
        return Result.success(financeRecordService.sumTotalIncome());
    }
    
    @GetMapping("/total/cost")
    public Result<BigDecimal> sumTotalCost() {
        return Result.success(financeRecordService.sumTotalCost());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<FinanceRecord> create(@RequestBody FinanceRecord record) {
        if (record.getRecordNo() != null && financeRecordService.existsByRecordNo(record.getRecordNo())) {
            return Result.error(409, "财务记录编号已存在");
        }
        return Result.success("创建成功", financeRecordService.save(record));
    }
    
    @PutMapping("/{id}")
    public Result<FinanceRecord> update(@PathVariable Long id, @RequestBody FinanceRecord record) {
        return financeRecordService.findById(id)
                .map(existing -> {
                    record.setId(id);
                    return Result.success("更新成功", financeRecordService.save(record));
                })
                .orElse(Result.error(404, "财务记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (financeRecordService.findById(id).isEmpty()) {
            return Result.error(404, "财务记录不存在");
        }
        financeRecordService.deleteById(id);
        return Result.success();
    }
}
