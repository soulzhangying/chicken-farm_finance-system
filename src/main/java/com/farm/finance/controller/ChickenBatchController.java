package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.ChickenBatch;
import com.farm.finance.service.ChickenBatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/chicken-batches")
@RequiredArgsConstructor
public class ChickenBatchController {
    
    private final ChickenBatchService chickenBatchService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<ChickenBatch>> findAll() {
        return Result.success(chickenBatchService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<ChickenBatch> findById(@PathVariable Long id) {
        return chickenBatchService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "批次不存在"));
    }
    
    @GetMapping("/batch-no/{batchNo}")
    public Result<ChickenBatch> findByBatchNo(@PathVariable String batchNo) {
        return chickenBatchService.findByBatchNo(batchNo)
                .map(Result::success)
                .orElse(Result.error(404, "批次不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/house/{houseId}")
    public Result<List<ChickenBatch>> findByHouseId(@PathVariable Long houseId) {
        return Result.success(chickenBatchService.findByHouseId(houseId));
    }
    
    @GetMapping("/supplier/{supplierId}")
    public Result<List<ChickenBatch>> findBySupplierId(@PathVariable Long supplierId) {
        return Result.success(chickenBatchService.findBySupplierId(supplierId));
    }
    
    @GetMapping("/group-name/{groupName}")
    public Result<List<ChickenBatch>> findByGroupName(@PathVariable String groupName) {
        return Result.success(chickenBatchService.findByGroupName(groupName));
    }
    
    @GetMapping("/breed/{breed}")
    public Result<List<ChickenBatch>> findByBreed(@PathVariable String breed) {
        return Result.success(chickenBatchService.findByBreed(breed));
    }
    
    @GetMapping("/status/{status}")
    public Result<List<ChickenBatch>> findByStatus(@PathVariable String status) {
        return Result.success(chickenBatchService.findByStatus(status));
    }
    
    @GetMapping("/active")
    public Result<List<ChickenBatch>> findActiveBatches() {
        return Result.success(chickenBatchService.findActiveBatches());
    }
    
    @GetMapping("/status/{status}/active")
    public Result<List<ChickenBatch>> findByStatusAndActive(@PathVariable String status) {
        return Result.success(chickenBatchService.findByStatus(status).stream()
                .filter(b -> Boolean.TRUE.equals(b.getIsActive()))
                .toList());
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<ChickenBatch>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(chickenBatchService.findByIsActive(isActive));
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/entry-date")
    public Result<List<ChickenBatch>> findByEntryDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(chickenBatchService.findByEntryDateBetween(startDate, endDate));
    }
    
    @GetMapping("/expected-sale-date")
    public Result<List<ChickenBatch>> findByExpectedSaleDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(chickenBatchService.findByExpectedSaleDateBetween(startDate, endDate));
    }
    
    @GetMapping("/actual-sale-date")
    public Result<List<ChickenBatch>> findByActualSaleDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(chickenBatchService.findByActualSaleDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<ChickenBatch>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenBatchService.findAll(pageable));
    }
    
    @GetMapping("/house/{houseId}/page")
    public Result<Page<ChickenBatch>> findByHouseIdPage(
            @PathVariable Long houseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(chickenBatchService.findByHouseId(houseId, pageable));
    }
    
    @GetMapping("/status/{status}/page")
    public Result<Page<ChickenBatch>> findByStatusPage(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(chickenBatchService.findByStatus(status, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<ChickenBatch>> search(
            @RequestParam(required = false) Long houseId,
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenBatchService.search(houseId, supplierId, status, isActive, pageable));
    }
    
    // ========== 特殊查询 ==========
    
    @GetMapping("/active/total-quantity")
    public Result<Integer> getTotalActiveQuantity() {
        return Result.success(chickenBatchService.getTotalActiveQuantity());
    }
    
    @GetMapping("/ready-for-sale")
    public Result<List<ChickenBatch>> findBatchesReadyForSale(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return Result.success(chickenBatchService.findBatchesReadyForSale(date));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<ChickenBatch>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(chickenBatchService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(chickenBatchService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/status/{status}")
    public Result<Long> countByStatus(@PathVariable String status) {
        return Result.success(chickenBatchService.countByStatus(status));
    }
    
    @GetMapping("/count/status/{status}/active")
    public Result<Long> countByStatusAndActive(@PathVariable String status) {
        return Result.success(chickenBatchService.countByStatusAndIsActiveTrue(status));
    }
    
    @GetMapping("/total/entry-quantity")
    public Result<Integer> getTotalEntryQuantity() {
        return Result.success(chickenBatchService.getTotalEntryQuantity());
    }
    
    @GetMapping("/total/current-quantity")
    public Result<Integer> getTotalCurrentQuantity() {
        return Result.success(chickenBatchService.getTotalCurrentQuantity());
    }
    
    @GetMapping("/total/death-count")
    public Result<Integer> getTotalDeathCount() {
        return Result.success(chickenBatchService.getTotalDeathCount());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<ChickenBatch> create(@RequestBody ChickenBatch batch) {
        if (chickenBatchService.existsByBatchNo(batch.getBatchNo())) {
            return Result.error(409, "批次号已存在");
        }
        return Result.success("创建成功", chickenBatchService.save(batch));
    }
    
    @PutMapping("/{id}")
    public Result<ChickenBatch> update(@PathVariable Long id, @RequestBody ChickenBatch batch) {
        return chickenBatchService.findById(id)
                .map(existing -> {
                    batch.setId(id);
                    return Result.success("更新成功", chickenBatchService.save(batch));
                })
                .orElse(Result.error(404, "批次不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (chickenBatchService.findById(id).isEmpty()) {
            return Result.error(404, "批次不存在");
        }
        chickenBatchService.deleteById(id);
        return Result.success();
    }
}
