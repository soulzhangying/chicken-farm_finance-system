package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.Inventory;
import com.farm.finance.service.InventoryService;
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
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {
    
    private final InventoryService inventoryService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<Inventory>> findAll() {
        return Result.success(inventoryService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<Inventory> findById(@PathVariable Long id) {
        return inventoryService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "库存记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/product/{productId}")
    public Result<List<Inventory>> findByProductId(@PathVariable Long productId) {
        return Result.success(inventoryService.findByProductId(productId));
    }
    
    @GetMapping("/batch-no/{batchNo}")
    public Result<List<Inventory>> findByBatchNo(@PathVariable String batchNo) {
        return Result.success(inventoryService.findByBatchNo(batchNo));
    }
    
    @GetMapping("/unit/{unit}")
    public Result<List<Inventory>> findByUnit(@PathVariable String unit) {
        return Result.success(inventoryService.findByUnit(unit));
    }
    
    @GetMapping("/location/{location}")
    public Result<List<Inventory>> findByLocation(@PathVariable String location) {
        return Result.success(inventoryService.findByLocation(location));
    }
    
    @GetMapping("/status/{status}")
    public Result<List<Inventory>> findByStatus(@PathVariable Boolean status) {
        return Result.success(inventoryService.findByStatus(status));
    }
    
    @GetMapping("/active")
    public Result<List<Inventory>> findByIsActiveTrue() {
        return Result.success(inventoryService.findByIsActiveTrue());
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<Inventory>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(inventoryService.findByIsActive(isActive));
    }
    
    @GetMapping("/product/{productId}/active")
    public Result<List<Inventory>> findActiveByProductId(@PathVariable Long productId) {
        return Result.success(inventoryService.findActiveByProductId(productId));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<Inventory>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(inventoryService.findAll(pageable));
    }
    
    @GetMapping("/product/{productId}/page")
    public Result<Page<Inventory>> findByProductIdPage(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(inventoryService.findByProductId(productId, pageable));
    }
    
    @GetMapping("/status/{status}/page")
    public Result<Page<Inventory>> findByStatusPage(
            @PathVariable Boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(inventoryService.findByStatus(status, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<Inventory>> search(
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(inventoryService.search(productId, status, isActive, pageable));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/product/{productId}/sum")
    public Result<BigDecimal> sumQuantityByProductId(@PathVariable Long productId) {
        return Result.success(inventoryService.sumQuantityByProductId(productId));
    }
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(inventoryService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/status-true")
    public Result<Long> countByStatusTrue() {
        return Result.success(inventoryService.countByStatusTrue());
    }
    
    @GetMapping("/total/quantity")
    public Result<BigDecimal> sumTotalQuantity() {
        return Result.success(inventoryService.sumTotalQuantity());
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<Inventory>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(inventoryService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<Inventory> create(@RequestBody Inventory inventory) {
        return Result.success("创建成功", inventoryService.save(inventory));
    }
    
    @PutMapping("/{id}")
    public Result<Inventory> update(@PathVariable Long id, @RequestBody Inventory inventory) {
        return inventoryService.findById(id)
                .map(existing -> {
                    inventory.setId(id);
                    return Result.success("更新成功", inventoryService.save(inventory));
                })
                .orElse(Result.error(404, "库存记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (inventoryService.findById(id).isEmpty()) {
            return Result.error(404, "库存记录不存在");
        }
        inventoryService.deleteById(id);
        return Result.success();
    }
}
