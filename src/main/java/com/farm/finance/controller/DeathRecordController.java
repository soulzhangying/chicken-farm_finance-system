package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.DeathRecord;
import com.farm.finance.service.DeathRecordService;
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
@RequestMapping("/api/death-records")
@RequiredArgsConstructor
public class DeathRecordController {
    
    private final DeathRecordService deathRecordService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<DeathRecord>> findAll() {
        return Result.success(deathRecordService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<DeathRecord> findById(@PathVariable Long id) {
        return deathRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "死亡记录不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/batch/{batchId}")
    public Result<List<DeathRecord>> findByBatchId(@PathVariable Long batchId) {
        return Result.success(deathRecordService.findByBatchId(batchId));
    }
    
    @GetMapping("/operator/{operatorId}")
    public Result<List<DeathRecord>> findByOperatorId(@PathVariable Long operatorId) {
        return Result.success(deathRecordService.findByOperatorId(operatorId));
    }
    
    @GetMapping("/reason/{deathReason}")
    public Result<List<DeathRecord>> findByDeathReason(@PathVariable String deathReason) {
        return Result.success(deathRecordService.findByDeathReason(deathReason));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<DeathRecord>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(deathRecordService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<DeathRecord>> findByIsActiveTrue() {
        return Result.success(deathRecordService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/death-date")
    public Result<List<DeathRecord>> findByDeathDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(deathRecordService.findByDeathDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<DeathRecord>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(deathRecordService.findAll(pageable));
    }
    
    @GetMapping("/batch/{batchId}/page")
    public Result<Page<DeathRecord>> findByBatchIdPage(
            @PathVariable Long batchId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(deathRecordService.findByBatchId(batchId, pageable));
    }
    
    @GetMapping("/operator/{operatorId}/page")
    public Result<Page<DeathRecord>> findByOperatorIdPage(
            @PathVariable Long operatorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(deathRecordService.findByOperatorId(operatorId, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<DeathRecord>> search(
            @RequestParam(required = false) Long batchId,
            @RequestParam(required = false) Long operatorId,
            @RequestParam(required = false) String deathReason,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(deathRecordService.search(batchId, operatorId, deathReason, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<DeathRecord>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(deathRecordService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(deathRecordService.countByIsActiveTrue());
    }
    
    @GetMapping("/batch/{batchId}/sum")
    public Result<Integer> sumDeathCountByBatchId(@PathVariable Long batchId) {
        return Result.success(deathRecordService.sumDeathCountByBatchId(batchId));
    }
    
    @GetMapping("/sum")
    public Result<Integer> sumDeathCountByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(deathRecordService.sumDeathCountByDateRange(startDate, endDate));
    }
    
    @GetMapping("/total")
    public Result<Integer> sumTotalDeathCount() {
        return Result.success(deathRecordService.sumTotalDeathCount());
    }
    
    @GetMapping("/group-by-reason")
    public Result<List<Object[]>> sumDeathCountGroupByReason() {
        return Result.success(deathRecordService.sumDeathCountGroupByReason());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<DeathRecord> create(@RequestBody DeathRecord record) {
        return Result.success("创建成功", deathRecordService.save(record));
    }
    
    @PutMapping("/{id}")
    public Result<DeathRecord> update(@PathVariable Long id, @RequestBody DeathRecord record) {
        return deathRecordService.findById(id)
                .map(existing -> {
                    record.setId(id);
                    return Result.success("更新成功", deathRecordService.save(record));
                })
                .orElse(Result.error(404, "死亡记录不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (deathRecordService.findById(id).isEmpty()) {
            return Result.error(404, "死亡记录不存在");
        }
        deathRecordService.deleteById(id);
        return Result.success();
    }
}
