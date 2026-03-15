package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.SalesOrderItem;
import com.farm.finance.service.SalesOrderItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/sales-order-items")
@RequiredArgsConstructor
public class SalesOrderItemController {
    
    private final SalesOrderItemService salesOrderItemService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<SalesOrderItem>> findAll() {
        return Result.success(salesOrderItemService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<SalesOrderItem> findById(@PathVariable Long id) {
        return salesOrderItemService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "销售订单明细不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/order/{orderId}")
    public Result<List<SalesOrderItem>> findByOrderId(@PathVariable Long orderId) {
        return Result.success(salesOrderItemService.findByOrderId(orderId));
    }
    
    @GetMapping("/product/{productId}")
    public Result<List<SalesOrderItem>> findByProductId(@PathVariable Long productId) {
        return Result.success(salesOrderItemService.findByProductId(productId));
    }
    
    @GetMapping("/batch-no/{batchNo}")
    public Result<List<SalesOrderItem>> findByBatchNo(@PathVariable String batchNo) {
        return Result.success(salesOrderItemService.findByBatchNo(batchNo));
    }
    
    @GetMapping("/unit/{unit}")
    public Result<List<SalesOrderItem>> findByUnit(@PathVariable String unit) {
        return Result.success(salesOrderItemService.findByUnit(unit));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<SalesOrderItem>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(salesOrderItemService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<SalesOrderItem>> findByIsActiveTrue() {
        return Result.success(salesOrderItemService.findByIsActiveTrue());
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<SalesOrderItem>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(salesOrderItemService.findAll(pageable));
    }
    
    @GetMapping("/order/{orderId}/page")
    public Result<Page<SalesOrderItem>> findByOrderIdPage(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(salesOrderItemService.findByOrderId(orderId, pageable));
    }
    
    @GetMapping("/product/{productId}/page")
    public Result<Page<SalesOrderItem>> findByProductIdPage(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(salesOrderItemService.findByProductId(productId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<SalesOrderItem>> search(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(salesOrderItemService.search(orderId, productId, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<SalesOrderItem>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(salesOrderItemService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(salesOrderItemService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/order/{orderId}")
    public Result<Long> countByOrderId(@PathVariable Long orderId) {
        return Result.success(salesOrderItemService.countByOrderId(orderId));
    }
    
    @GetMapping("/order/{orderId}/sum/total-price")
    public Result<BigDecimal> sumTotalPriceByOrderId(@PathVariable Long orderId) {
        return Result.success(salesOrderItemService.sumTotalPriceByOrderId(orderId));
    }
    
    @GetMapping("/order/{orderId}/sum/quantity")
    public Result<BigDecimal> sumQuantityByOrderId(@PathVariable Long orderId) {
        return Result.success(salesOrderItemService.sumQuantityByOrderId(orderId));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<SalesOrderItem> create(@RequestBody SalesOrderItem item) {
        return Result.success("创建成功", salesOrderItemService.save(item));
    }
    
    @PutMapping("/{id}")
    public Result<SalesOrderItem> update(@PathVariable Long id, @RequestBody SalesOrderItem item) {
        return salesOrderItemService.findById(id)
                .map(existing -> {
                    item.setId(id);
                    return Result.success("更新成功", salesOrderItemService.save(item));
                })
                .orElse(Result.error(404, "销售订单明细不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (salesOrderItemService.findById(id).isEmpty()) {
            return Result.error(404, "销售订单明细不存在");
        }
        salesOrderItemService.deleteById(id);
        return Result.success();
    }
}
