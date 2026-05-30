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
import java.util.List;

@RestController
@RequestMapping("/api/chicken-batches")
@RequiredArgsConstructor
public class ChickenBatchController {
    
    private final ChickenBatchService chickenBatchService;
    
    @GetMapping
    public Result<List<ChickenBatch>> findAll() {
        return Result.success(chickenBatchService.findAllActive());
    }
    
    @GetMapping("/{id}")
    public Result<ChickenBatch> findById(@PathVariable Long id) {
        return chickenBatchService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "批次不存在"));
    }
    
    @GetMapping("/house/{houseId}")
    public Result<List<ChickenBatch>> findByHouseId(@PathVariable Long houseId) {
        return Result.success(chickenBatchService.findByHouseId(houseId));
    }
    
    @GetMapping("/status/{status}")
    public Result<List<ChickenBatch>> findByStatus(@PathVariable String status) {
        return Result.success(chickenBatchService.findByStatus(status));
    }
    
    @GetMapping("/search")
    public Result<Page<ChickenBatch>> search(
            @RequestParam(required = false) Long houseId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(chickenBatchService.search(houseId, status, pageable));
    }
    
    @GetMapping("/search/keyword")
    public Result<List<ChickenBatch>> searchByKeyword(@RequestParam String keyword) {
        return Result.success(chickenBatchService.searchByKeyword(keyword));
    }
    
    @PostMapping
    public Result<ChickenBatch> create(@RequestBody ChickenBatch batch) {
        if (batch.getBatchNo() != null && chickenBatchService.existsByBatchNo(batch.getBatchNo())) {
            return Result.error(409, "批次编号已存在");
        }
        if (batch.getBatchName() != null && chickenBatchService.existsByBatchName(batch.getBatchName())) {
            return Result.error(409, "批次名称已存在");
        }
        return Result.success("创建成功", chickenBatchService.save(batch));
    }
    
    @PutMapping("/{id}")
    public Result<ChickenBatch> update(@PathVariable Long id, @RequestBody ChickenBatch batch) {
        return chickenBatchService.findById(id)
                .map(existing -> {
                    batch.setId(id);
                    batch.setCreatedTime(existing.getCreatedTime());
                    batch.setIsActive(existing.getIsActive());
                    return Result.success("更新成功", chickenBatchService.save(batch));
                })
                .orElse(Result.error(404, "批次不存在"));
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (chickenBatchService.findById(id).isEmpty()) {
            return Result.error(404, "批次不存在");
        }
        chickenBatchService.deleteById(id);
        return Result.success("删除成功", null);
    }
}
