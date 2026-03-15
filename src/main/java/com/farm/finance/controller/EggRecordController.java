package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.EggRecord;
import com.farm.finance.service.EggRecordService;
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
@RequestMapping("/api/egg-records")
@RequiredArgsConstructor
public class EggRecordController {
    
    private final EggRecordService eggRecordService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<EggRecord>> findAll() {
        return Result.success(eggRecordService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<EggRecord> findById(@PathVariable Long id) {
        return eggRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "产蛋记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/batch/{batchId}")
    public Result<List<EggRecord>> findByBatchId(@PathVariable Long batchId) {
        return Result.success(eggRecordService.findByBatchId(batchId));
    }
    
    @GetMapping("/product/{productId}")
    public Result<List<EggRecord>> findByProductId(@PathVariable Long productId) {
        return Result.success(eggRecordService.findByProductId(productId));
    }
    
    @GetMapping("/operator/{operatorId}")
    public Result<List<EggRecord>> findByOperatorId(@PathVariable Long operatorId) {
        return Result.success(eggRecordService.findByOperatorId(operatorId));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<EggRecord>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(eggRecordService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<EggRecord>> findByIsActiveTrue() {
        return Result.success(eggRecordService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/production-date")
    public Result<List<EggRecord>> findByProductionDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(eggRecordService.findByProductionDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<EggRecord>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(eggRecordService.findAll(pageable));
    }
    
    @GetMapping("/batch/{batchId}/page")
    public Result<Page<EggRecord>> findByBatchIdPage(
            @PathVariable Long batchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(eggRecordService.findByBatchId(batchId, pageable));
    }
    
    @GetMapping("/product/{productId}/page")
    public Result<Page<EggRecord>> findByProductIdPage(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(eggRecordService.findByProductId(productId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<EggRecord>> search(
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) Long productId,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(eggRecordService.search(batchId, productId, operatorId, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<EggRecord>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(eggRecordService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(eggRecordService.countByIsActiveTrue());
    }
    
    @GetMapping("/batch/{batchId}/sum")
    public Result<Integer> sumCountByBatchId(@PathVariable Long batchId) {
        return Result.success(eggRecordService.sumCountByBatchId(batchId));
    }
    
    @GetMapping("/sum")
    public Result<Integer> sumCountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(eggRecordService.sumCountByDateRange(startDate, endDate));
    }
    
    @GetMapping("/total/count")
    public Result<Integer> sumTotalCount() {
        return Result.success(eggRecordService.sumTotalCount());
    }
    
    @GetMapping("/total/weight")
    public Result<BigDecimal> sumTotalWeight() {
        return Result.success(eggRecordService.sumTotalWeight());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<EggRecord> create(@RequestBody EggRecord record) {
        return Result.success("创建成功", eggRecordService.save(record));
    }
    
    @PutMapping("/{id}")
    public Result<EggRecord> update(@PathVariable Long id, @RequestBody EggRecord record) {
        return eggRecordService.findById(id)
                .map(existing -> {
                    record.setId(id);
                    return Result.success("更新成功", eggRecordService.save(record));
                })
                .orElse(Result.error(404, "产蛋记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (eggRecordService.findById(id).isEmpty()) {
            return Result.error(404, "产蛋记录不存在");
        }
        eggRecordService.deleteById(id);
        return Result.success();
    }
}
