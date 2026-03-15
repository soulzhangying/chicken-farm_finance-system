package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.PurchaseOrderItem;
import com.farm.finance.service.PurchaseOrderItemService;
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
@RequestMapping("/api/purchase-order-items")
@RequiredArgsConstructor
public class PurchaseOrderItemController {
    
    private final PurchaseOrderItemService purchaseOrderItemService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<PurchaseOrderItem>> findAll() {
        return Result.success(purchaseOrderItemService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<PurchaseOrderItem> findById(@PathVariable Long id) {
        return purchaseOrderItemService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "采购订单明细不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/order/{orderId}")
    public Result<List<PurchaseOrderItem>> findByOrderId(@PathVariable Long orderId) {
        return Result.success(purchaseOrderItemService.findByOrderId(orderId));
    }
    
    @GetMapping("/product/{productId}")
    public Result<List<PurchaseOrderItem>> findByProductId(@PathVariable Long productId) {
        return Result.success(purchaseOrderItemService.findByProductId(productId));
    }
    
    @GetMapping("/unit/{unit}")
    public Result<List<PurchaseOrderItem>> findByUnit(@PathVariable String unit) {
        return Result.success(purchaseOrderItemService.findByUnit(unit));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<PurchaseOrderItem>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(purchaseOrderItemService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<PurchaseOrderItem>> findByIsActiveTrue() {
        return Result.success(purchaseOrderItemService.findByIsActiveTrue());
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<PurchaseOrderItem>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(purchaseOrderItemService.findAll(pageable));
    }
    
    @GetMapping("/order/{orderId}/page")
    public Result<Page<PurchaseOrderItem>> findByOrderIdPage(
            @PathVariable Long orderId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(purchaseOrderItemService.findByOrderId(orderId, pageable));
    }
    
    @GetMapping("/product/{productId}/page")
    public Result<Page<PurchaseOrderItem>> findByProductIdPage(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(purchaseOrderItemService.findByProductId(productId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<PurchaseOrderItem>> search(
            @RequestParam(required = false) Long orderId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(purchaseOrderItemService.search(orderId, productId, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<PurchaseOrderItem>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(purchaseOrderItemService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(purchaseOrderItemService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/order/{orderId}")
    public Result<Long> countByOrderId(@PathVariable Long orderId) {
        return Result.success(purchaseOrderItemService.countByOrderId(orderId));
    }
    
    @GetMapping("/order/{orderId}/sum/total-price")
    public Result<BigDecimal> sumTotalPriceByOrderId(@PathVariable Long orderId) {
        return Result.success(purchaseOrderItemService.sumTotalPriceByOrderId(orderId));
    }
    
    @GetMapping("/order/{orderId}/sum/quantity")
    public Result<BigDecimal> sumQuantityByOrderId(@PathVariable Long orderId) {
        return Result.success(purchaseOrderItemService.sumQuantityByOrderId(orderId));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<PurchaseOrderItem> create(@RequestBody PurchaseOrderItem item) {
        return Result.success("创建成功", purchaseOrderItemService.save(item));
    }
    
    @PutMapping("/{id}")
    public Result<PurchaseOrderItem> update(@PathVariable Long id, @RequestBody PurchaseOrderItem item) {
        return purchaseOrderItemService.findById(id)
                .map(existing -> {
                    item.setId(id);
                    return Result.success("更新成功", purchaseOrderItemService.save(item));
                })
                .orElse(Result.error(404, "采购订单明细不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (purchaseOrderItemService.findById(id).isEmpty()) {
            return Result.error(404, "采购订单明细不存在");
        }
        purchaseOrderItemService.deleteById(id);
        return Result.success();
    }
}
