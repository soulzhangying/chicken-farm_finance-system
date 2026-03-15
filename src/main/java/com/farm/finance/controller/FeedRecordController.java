package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.FeedRecord;
import com.farm.finance.service.FeedRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/feed-records")
@RequiredArgsConstructor
public class FeedRecordController {
    
    private final FeedRecordService feedRecordService;
    
    @GetMapping
    public Result<List<FeedRecord>> findAll() {
        return Result.success(feedRecordService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<FeedRecord> findById(@PathVariable Long id) {
        return feedRecordService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "饲料记录不存在"));
    }
    
    @GetMapping("/batch/{batchId}")
    public Result<List<FeedRecord>> findByBatchId(@PathVariable Long batchId) {
        return Result.success(feedRecordService.findByBatchId(batchId));
    }
    
    @GetMapping("/product/{productId}")
    public Result<List<FeedRecord>> findByProductId(@PathVariable Long productId) {
        return Result.success(feedRecordService.findByProductId(productId));
    }
    
    @GetMapping("/batch/{batchId}/sum")
    public Result<BigDecimal> sumCostByBatchId(@PathVariable Long batchId) {
        return Result.success(feedRecordService.sumCostByBatchId(batchId));
    }
    
    @GetMapping("/sum")
    public Result<BigDecimal> sumCostByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(feedRecordService.sumCostByDateRange(startDate, endDate));
    }
    
    @PostMapping
    public Result<FeedRecord> create(@RequestBody FeedRecord record) {
        return Result.success("创建成功", feedRecordService.save(record));
    }
    
    @PutMapping("/{id}")
    public Result<FeedRecord> update(@PathVariable Long id, @RequestBody FeedRecord record) {
        return feedRecordService.findById(id)
                .map(existing -> {
                    record.setId(id);
                    return Result.success("更新成功", feedRecordService.save(record));
                })
                .orElse(Result.error(404, "饲料记录不存在"));
    }
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (feedRecordService.findById(id).isEmpty()) {
            return Result.error(404, "饲料记录不存在");
        }
        feedRecordService.deleteById(id);
        return Result.success();
    }
}