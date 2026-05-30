package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.ChickenSale;
import com.farm.finance.service.ChickenSaleService;
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
@RequestMapping("/api/chicken-sales")
@RequiredArgsConstructor
public class ChickenSaleController {
    
    private final ChickenSaleService chickenSaleService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<ChickenSale>> findAll() {
        return Result.success(chickenSaleService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<ChickenSale> findById(@PathVariable Long id) {
        return chickenSaleService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "肉鸡销售记录不存在"));
    }
    
    @GetMapping("/sale-no/{saleNo}")
    public Result<ChickenSale> findBySaleNo(@PathVariable String saleNo) {
        return chickenSaleService.findBySaleNo(saleNo)
                .map(Result::success)
                .orElse(Result.error(404, "肉鸡销售记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/batch/{batchId}")
    public Result<List<ChickenSale>> findByBatchId(@PathVariable Long batchId) {
        return Result.success(chickenSaleService.findByBatchId(batchId));
    }
    
    @GetMapping("/customer/{customerId}")
    public Result<List<ChickenSale>> findByCustomerId(@PathVariable Long customerId) {
        return Result.success(chickenSaleService.findByCustomerId(customerId));
    }
    
    @GetMapping("/sale-date/{saleDate}")
    public Result<List<ChickenSale>> findBySaleDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate saleDate) {
        return Result.success(chickenSaleService.findBySaleDate(saleDate));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<ChickenSale>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(chickenSaleService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<ChickenSale>> findByIsActiveTrue() {
        return Result.success(chickenSaleService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/sale-date-range")
    public Result<List<ChickenSale>> findBySaleDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(chickenSaleService.findBySaleDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<ChickenSale>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenSaleService.findAll(pageable));
    }
    
    @GetMapping("/batch/{batchId}/page")
    public Result<Page<ChickenSale>> findByBatchIdPage(
            @PathVariable Long batchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(chickenSaleService.findByBatchId(batchId, pageable));
    }
    
    @GetMapping("/customer/{customerId}/page")
    public Result<Page<ChickenSale>> findByCustomerIdPage(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(chickenSaleService.findByCustomerId(customerId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<ChickenSale>> search(
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenSaleService.search(batchId, customerId, isActive, pageable));
    }
    
    @GetMapping("/search/keyword")
    public Result<List<ChickenSale>> searchByKeyword(@RequestParam String keyword) {
        return Result.success(chickenSaleService.searchByKeyword(keyword));
    }
    
    @GetMapping("/search/date-range")
    public Result<List<ChickenSale>> searchByDateRange(
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(chickenSaleService.searchByBatchIdAndDateRange(batchId, startDate, endDate));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<ChickenSale>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(chickenSaleService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(chickenSaleService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/batch/{batchId}")
    public Result<Long> countByBatchId(@PathVariable Long batchId) {
        return Result.success(chickenSaleService.countByBatchId(batchId));
    }
    
    @GetMapping("/total/amount")
    public Result<BigDecimal> sumTotalAmount() {
        return Result.success(chickenSaleService.sumTotalAmount());
    }
    
    @GetMapping("/batch/{batchId}/sum/quantity")
    public Result<Integer> sumQuantityByBatchId(@PathVariable Long batchId) {
        return Result.success(chickenSaleService.sumQuantityByBatchId(batchId));
    }
    
    @GetMapping("/batch/{batchId}/sum/weight")
    public Result<BigDecimal> sumWeightByBatchId(@PathVariable Long batchId) {
        return Result.success(chickenSaleService.sumWeightByBatchId(batchId));
    }
    
    @GetMapping("/batch/{batchId}/sum/amount")
    public Result<BigDecimal> sumTotalAmountByBatchId(@PathVariable Long batchId) {
        return Result.success(chickenSaleService.sumTotalAmountByBatchId(batchId));
    }
    
    // ========== 按批次汇总销售额 ==========
    
    @GetMapping("/summary/by-batch")
    public Result<List<Object[]>> sumByBatchId() {
        return Result.success(chickenSaleService.sumByBatchId());
    }
    
    @GetMapping("/summary/by-batch/date-range")
    public Result<List<Object[]>> sumByBatchIdAndDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(chickenSaleService.sumByBatchIdAndDateRange(startDate, endDate));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<ChickenSale> create(@RequestBody ChickenSale sale) {
        if (sale.getSaleNo() != null && chickenSaleService.existsBySaleNo(sale.getSaleNo())) {
            return Result.error(409, "销售单号已存在");
        }
        return Result.success("创建成功", chickenSaleService.save(sale));
    }
    
    @PutMapping("/{id}")
    public Result<ChickenSale> update(@PathVariable Long id, @RequestBody ChickenSale sale) {
        return chickenSaleService.findById(id)
                .map(existing -> {
                    sale.setId(id);
                    return Result.success("更新成功", chickenSaleService.save(sale));
                })
                .orElse(Result.error(404, "肉鸡销售记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (chickenSaleService.findById(id).isEmpty()) {
            return Result.error(404, "肉鸡销售记录不存在");
        }
        chickenSaleService.deleteById(id);
        return Result.success();
    }
}
