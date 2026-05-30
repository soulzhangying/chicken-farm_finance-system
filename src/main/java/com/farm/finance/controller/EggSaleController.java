package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.EggSale;
import com.farm.finance.service.EggSaleService;
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
@RequestMapping("/api/egg-sales")
@RequiredArgsConstructor
public class EggSaleController {
    
    private final EggSaleService eggSaleService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<EggSale>> findAll() {
        return Result.success(eggSaleService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<EggSale> findById(@PathVariable Long id) {
        return eggSaleService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "鸡蛋销售记录不存在"));
    }
    
    @GetMapping("/sale-no/{saleNo}")
    public Result<EggSale> findBySaleNo(@PathVariable String saleNo) {
        return eggSaleService.findBySaleNo(saleNo)
                .map(Result::success)
                .orElse(Result.error(404, "销售记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/product/{productId}")
    public Result<List<EggSale>> findByProductId(@PathVariable Long productId) {
        return Result.success(eggSaleService.findByProductId(productId));
    }
    
    @GetMapping("/customer/{customerId}")
    public Result<List<EggSale>> findByCustomerId(@PathVariable Long customerId) {
        return Result.success(eggSaleService.findByCustomerId(customerId));
    }
    
    @GetMapping("/created-by/{createdBy}")
    public Result<List<EggSale>> findByCreatedBy(@PathVariable Long createdBy) {
        return Result.success(eggSaleService.findByCreatedBy(createdBy));
    }
    
    @GetMapping("/payment-method/{paymentMethod}")
    public Result<List<EggSale>> findByPaymentMethod(@PathVariable String paymentMethod) {
        return Result.success(eggSaleService.findByPaymentMethod(paymentMethod));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<EggSale>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(eggSaleService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<EggSale>> findByIsActiveTrue() {
        return Result.success(eggSaleService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/sale-date")
    public Result<List<EggSale>> findBySaleDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(eggSaleService.findBySaleDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<EggSale>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(eggSaleService.findAll(pageable));
    }
    
    @GetMapping("/product/{productId}/page")
    public Result<Page<EggSale>> findByProductIdPage(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(eggSaleService.findByProductId(productId, pageable));
    }
    
    @GetMapping("/customer/{customerId}/page")
    public Result<Page<EggSale>> findByCustomerIdPage(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(eggSaleService.findByCustomerId(customerId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<EggSale>> search(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(eggSaleService.search(productId, customerId, paymentMethod, isActive, pageable));
    }
    
    @GetMapping("/search/keyword")
    public Result<List<EggSale>> searchByKeyword(@RequestParam String keyword) {
        return Result.success(eggSaleService.searchByKeyword(keyword));
    }
    
    @GetMapping("/search/date-range")
    public Result<List<EggSale>> searchByDateRange(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(eggSaleService.searchByDateRange(startDate, endDate));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<EggSale>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(eggSaleService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(eggSaleService.countByIsActiveTrue());
    }
    
    @GetMapping("/sum/amount")
    public Result<BigDecimal> sumAmountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(eggSaleService.sumAmountByDateRange(startDate, endDate));
    }
    
    @GetMapping("/sum/quantity")
    public Result<Integer> sumQuantityByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(eggSaleService.sumQuantityByDateRange(startDate, endDate));
    }
    
    @GetMapping("/total/amount")
    public Result<BigDecimal> sumTotalAmount() {
        return Result.success(eggSaleService.sumTotalAmount());
    }
    
    @GetMapping("/total/quantity")
    public Result<Integer> sumTotalQuantity() {
        return Result.success(eggSaleService.sumTotalQuantity());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<EggSale> create(@RequestBody EggSale sale) {
        if (sale.getSaleNo() != null && eggSaleService.existsBySaleNo(sale.getSaleNo())) {
            return Result.error(409, "销售单号已存在");
        }
        return Result.success("创建成功", eggSaleService.save(sale));
    }
    
    @PutMapping("/{id}")
    public Result<EggSale> update(@PathVariable Long id, @RequestBody EggSale sale) {
        return eggSaleService.findById(id)
                .map(existing -> {
                    sale.setId(id);
                    return Result.success("更新成功", eggSaleService.save(sale));
                })
                .orElse(Result.error(404, "鸡蛋销售记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (eggSaleService.findById(id).isEmpty()) {
            return Result.error(404, "鸡蛋销售记录不存在");
        }
        eggSaleService.deleteById(id);
        return Result.success();
    }
}
