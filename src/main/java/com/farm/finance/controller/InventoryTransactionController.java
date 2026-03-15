package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.InventoryTransaction;
import com.farm.finance.service.InventoryTransactionService;
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
@RequestMapping("/api/inventory-transactions")
@RequiredArgsConstructor
public class InventoryTransactionController {
    
    private final InventoryTransactionService inventoryTransactionService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<InventoryTransaction>> findAll() {
        return Result.success(inventoryTransactionService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<InventoryTransaction> findById(@PathVariable Long id) {
        return inventoryTransactionService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "库存交易记录不存在"));
    }
    
    @GetMapping("/transaction-no/{transactionNo}")
    public Result<InventoryTransaction> findByTransactionNo(@PathVariable String transactionNo) {
        return inventoryTransactionService.findByTransactionNo(transactionNo)
                .map(Result::success)
                .orElse(Result.error(404, "交易记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/type/{transactionType}")
    public Result<List<InventoryTransaction>> findByTransactionType(@PathVariable String transactionType) {
        return Result.success(inventoryTransactionService.findByTransactionType(transactionType));
    }
    
    @GetMapping("/product/{productId}")
    public Result<List<InventoryTransaction>> findByProductId(@PathVariable Long productId) {
        return Result.success(inventoryTransactionService.findByProductId(productId));
    }
    
    @GetMapping("/batch-no/{batchNo}")
    public Result<List<InventoryTransaction>> findByBatchNo(@PathVariable String batchNo) {
        return Result.success(inventoryTransactionService.findByBatchNo(batchNo));
    }
    
    @GetMapping("/operator/{operatorId}")
    public Result<List<InventoryTransaction>> findByOperatorId(@PathVariable Long operatorId) {
        return Result.success(inventoryTransactionService.findByOperatorId(operatorId));
    }
    
    @GetMapping("/related-money/{relatedMoneyId}")
    public Result<List<InventoryTransaction>> findByRelatedMoneyId(@PathVariable Long relatedMoneyId) {
        return Result.success(inventoryTransactionService.findByRelatedMoneyId(relatedMoneyId));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<InventoryTransaction>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(inventoryTransactionService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<InventoryTransaction>> findByIsActiveTrue() {
        return Result.success(inventoryTransactionService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/transaction-date")
    public Result<List<InventoryTransaction>> findByTransactionDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(inventoryTransactionService.findByTransactionDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<InventoryTransaction>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(inventoryTransactionService.findAll(pageable));
    }
    
    @GetMapping("/product/{productId}/page")
    public Result<Page<InventoryTransaction>> findByProductIdPage(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(inventoryTransactionService.findByProductId(productId, pageable));
    }
    
    @GetMapping("/type/{transactionType}/page")
    public Result<Page<InventoryTransaction>> findByTransactionTypePage(
            @PathVariable String transactionType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(inventoryTransactionService.findByTransactionType(transactionType, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<InventoryTransaction>> search(
            @RequestParam(required = false) String transactionType,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(inventoryTransactionService.search(transactionType, productId, operatorId, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<InventoryTransaction>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(inventoryTransactionService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(inventoryTransactionService.countByIsActiveTrue());
    }
    
    @GetMapping("/type/{transactionType}/sum/quantity")
    public Result<BigDecimal> sumQuantityByTransactionType(@PathVariable String transactionType) {
        return Result.success(inventoryTransactionService.sumQuantityByTransactionType(transactionType));
    }
    
    @GetMapping("/type/{transactionType}/sum/amount")
    public Result<BigDecimal> sumAmountByTransactionType(@PathVariable String transactionType) {
        return Result.success(inventoryTransactionService.sumAmountByTransactionType(transactionType));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<InventoryTransaction> create(@RequestBody InventoryTransaction transaction) {
        if (transaction.getTransactionNo() != null && inventoryTransactionService.existsByTransactionNo(transaction.getTransactionNo())) {
            return Result.error(409, "交易单号已存在");
        }
        return Result.success("创建成功", inventoryTransactionService.save(transaction));
    }
    
    @PutMapping("/{id}")
    public Result<InventoryTransaction> update(@PathVariable Long id, @RequestBody InventoryTransaction transaction) {
        return inventoryTransactionService.findById(id)
                .map(existing -> {
                    transaction.setId(id);
                    return Result.success("更新成功", inventoryTransactionService.save(transaction));
                })
                .orElse(Result.error(404, "库存交易记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (inventoryTransactionService.findById(id).isEmpty()) {
            return Result.error(404, "库存交易记录不存在");
        }
        inventoryTransactionService.deleteById(id);
        return Result.success();
    }
}
