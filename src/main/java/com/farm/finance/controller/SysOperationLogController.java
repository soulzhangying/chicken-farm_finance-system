package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.SysOperationLog;
import com.farm.finance.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/operation-logs")
@RequiredArgsConstructor
public class SysOperationLogController {
    
    private final SysOperationLogService sysOperationLogService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<SysOperationLog>> findAll() {
        return Result.success(sysOperationLogService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<SysOperationLog> findById(@PathVariable Long id) {
        return sysOperationLogService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "操作日志不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/user/{userId}")
    public Result<List<SysOperationLog>> findByUserId(@PathVariable Long userId) {
        return Result.success(sysOperationLogService.findByUserId(userId));
    }
    
    @GetMapping("/username/{username}")
    public Result<List<SysOperationLog>> findByUsername(@PathVariable String username) {
        return Result.success(sysOperationLogService.findByUsername(username));
    }
    
    @GetMapping("/operation/{operation}")
    public Result<List<SysOperationLog>> findByOperation(@PathVariable String operation) {
        return Result.success(sysOperationLogService.findByOperation(operation));
    }
    
    @GetMapping("/ip/{ip}")
    public Result<List<SysOperationLog>> findByIp(@PathVariable String ip) {
        return Result.success(sysOperationLogService.findByIp(ip));
    }
    
    @GetMapping("/status/{status}")
    public Result<List<SysOperationLog>> findByStatus(@PathVariable Boolean status) {
        return Result.success(sysOperationLogService.findByStatus(status));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<SysOperationLog>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(sysOperationLogService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<SysOperationLog>> findByIsActiveTrue() {
        return Result.success(sysOperationLogService.findByIsActiveTrue());
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<SysOperationLog>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(sysOperationLogService.findAll(pageable));
    }
    
    @GetMapping("/user/{userId}/page")
    public Result<Page<SysOperationLog>> findByUserIdPage(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(sysOperationLogService.findByUserId(userId, pageable));
    }
    
    @GetMapping("/operation/{operation}/page")
    public Result<Page<SysOperationLog>> findByOperationPage(
            @PathVariable String operation,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(sysOperationLogService.findByOperation(operation, pageable));
    }
    
    @GetMapping("/status/{status}/page")
    public Result<Page<SysOperationLog>> findByStatusPage(
            @PathVariable Boolean status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(sysOperationLogService.findByStatus(status, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<SysOperationLog>> search(
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Boolean status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(sysOperationLogService.search(userId, operation, status, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<SysOperationLog>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(sysOperationLogService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(sysOperationLogService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/user/{userId}")
    public Result<Long> countByUserId(@PathVariable Long userId) {
        return Result.success(sysOperationLogService.countByUserId(userId));
    }
    
    @GetMapping("/count/operation/{operation}")
    public Result<Long> countByOperation(@PathVariable String operation) {
        return Result.success(sysOperationLogService.countByOperation(operation));
    }
    
    @GetMapping("/count/status/{status}")
    public Result<Long> countByStatus(@PathVariable Boolean status) {
        return Result.success(sysOperationLogService.countByStatus(status));
    }
    
    // ========== 创建 ==========
    
    @PostMapping
    public Result<SysOperationLog> create(@RequestBody SysOperationLog log) {
        return Result.success("创建成功", sysOperationLogService.save(log));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (sysOperationLogService.findById(id).isEmpty()) {
            return Result.error(404, "操作日志不存在");
        }
        sysOperationLogService.deleteById(id);
        return Result.success();
    }
}
