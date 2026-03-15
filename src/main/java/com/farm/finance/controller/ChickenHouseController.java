package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.ChickenHouse;
import com.farm.finance.service.ChickenHouseService;
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
@RequestMapping("/api/chicken-houses")
@RequiredArgsConstructor
public class ChickenHouseController {
    
    private final ChickenHouseService chickenHouseService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<ChickenHouse>> findAll() {
        return Result.success(chickenHouseService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<ChickenHouse> findById(@PathVariable Long id) {
        return chickenHouseService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "鸡舍不存在"));
    }
    
    @GetMapping("/house-no/{houseNo}")
    public Result<ChickenHouse> findByHouseNo(@PathVariable String houseNo) {
        return chickenHouseService.findByHouseNo(houseNo)
                .map(Result::success)
                .orElse(Result.error(404, "鸡舍不存在"));
    }
    
    @GetMapping("/name/{name}")
    public Result<ChickenHouse> findByName(@PathVariable String name) {
        return chickenHouseService.findByName(name)
                .map(Result::success)
                .orElse(Result.error(404, "鸡舍不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/status/{status}")
    public Result<List<ChickenHouse>> findByStatus(@PathVariable String status) {
        return Result.success(chickenHouseService.findByStatus(status));
    }
    
    @GetMapping("/capacity/{capacity}")
    public Result<List<ChickenHouse>> findByCapacity(@PathVariable Integer capacity) {
        return Result.success(chickenHouseService.findByCapacity(capacity));
    }
    
    @GetMapping("/capacity")
    public Result<List<ChickenHouse>> findByCapacityBetween(
            @RequestParam Integer minCapacity,
            @RequestParam Integer maxCapacity) {
        return Result.success(chickenHouseService.findByCapacityBetween(minCapacity, maxCapacity));
    }
    
    @GetMapping("/area")
    public Result<List<ChickenHouse>> findByAreaBetween(
            @RequestParam BigDecimal minArea,
            @RequestParam BigDecimal maxArea) {
        return Result.success(chickenHouseService.findByAreaBetween(minArea, maxArea));
    }
    
    @GetMapping("/active")
    public Result<List<ChickenHouse>> findActiveHouses() {
        return Result.success(chickenHouseService.findActiveHouses());
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<ChickenHouse>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(chickenHouseService.findByIsActive(isActive));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<ChickenHouse>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenHouseService.findAll(pageable));
    }
    
    @GetMapping("/status/{status}/page")
    public Result<Page<ChickenHouse>> findByStatusPage(
            @PathVariable String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(chickenHouseService.findByStatus(status, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<ChickenHouse>> search(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenHouseService.search(status, isActive, pageable));
    }
    
    // ========== 特殊查询 ==========
    
    @GetMapping("/total-capacity")
    public Result<Integer> getTotalCapacity() {
        return Result.success(chickenHouseService.getTotalCapacity());
    }
    
    @GetMapping("/total-area")
    public Result<BigDecimal> getTotalArea() {
        return Result.success(chickenHouseService.getTotalArea());
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<ChickenHouse>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(chickenHouseService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(chickenHouseService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/status/{status}")
    public Result<Long> countByStatus(@PathVariable String status) {
        return Result.success(chickenHouseService.countByStatus(status));
    }
    
    @GetMapping("/count/status/{status}/active")
    public Result<Long> countByStatusAndActive(@PathVariable String status) {
        return Result.success(chickenHouseService.countByStatusAndIsActiveTrue(status));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<ChickenHouse> create(@RequestBody ChickenHouse house) {
        if (chickenHouseService.existsByHouseNo(house.getHouseNo())) {
            return Result.error(409, "鸡舍编号已存在");
        }
        if (chickenHouseService.existsByName(house.getName())) {
            return Result.error(409, "鸡舍名称已存在");
        }
        return Result.success("创建成功", chickenHouseService.save(house));
    }
    
    @PutMapping("/{id}")
    public Result<ChickenHouse> update(@PathVariable Long id, @RequestBody ChickenHouse house) {
        return chickenHouseService.findById(id)
                .map(existing -> {
                    house.setId(id);
                    return Result.success("更新成功", chickenHouseService.save(house));
                })
                .orElse(Result.error(404, "鸡舍不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (chickenHouseService.findById(id).isEmpty()) {
            return Result.error(404, "鸡舍不存在");
        }
        chickenHouseService.deleteById(id);
        return Result.success();
    }
}
