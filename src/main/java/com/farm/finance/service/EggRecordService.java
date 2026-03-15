package com.farm.finance.service;

import com.farm.finance.entity.EggRecord;
import com.farm.finance.repository.EggRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class EggRecordService {
    
    private final EggRecordRepository eggRecordRepository;
    
    // ========== 基本查询 ==========
    
    public List<EggRecord> findAll() {
        return eggRecordRepository.findAll();
    }
    
    public Page<EggRecord> findAll(Pageable pageable) {
        return eggRecordRepository.findAll(pageable);
    }
    
    public Optional<EggRecord> findById(Long id) {
        return eggRecordRepository.findById(id);
    }
    
    // ========== 按字段查询 ==========
    
    public List<EggRecord> findByBatchId(Long batchId) {
        return eggRecordRepository.findByBatchId(batchId);
    }
    
    public List<EggRecord> findByProductId(Long productId) {
        return eggRecordRepository.findByProductId(productId);
    }
    
    public List<EggRecord> findByOperatorId(Long operatorId) {
        return eggRecordRepository.findByOperatorId(operatorId);
    }
    
    public List<EggRecord> findByIsActive(Boolean isActive) {
        return eggRecordRepository.findByIsActive(isActive);
    }
    
    public List<EggRecord> findByIsActiveTrue() {
        return eggRecordRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<EggRecord> findByProductionDateBetween(LocalDate startDate, LocalDate endDate) {
        return eggRecordRepository.findByProductionDateBetween(startDate, endDate);
    }
    
    // ========== 自定义查询 ==========
    
    public List<EggRecord> findByBatchIdOrderByDateDesc(Long batchId) {
        return eggRecordRepository.findByBatchIdOrderByDateDesc(batchId);
    }
    
    // ========== 分页查询 ==========
    
    public Page<EggRecord> findByBatchId(Long batchId, Pageable pageable) {
        return eggRecordRepository.findByBatchId(batchId, pageable);
    }
    
    public Page<EggRecord> findByProductId(Long productId, Pageable pageable) {
        return eggRecordRepository.findByProductId(productId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<EggRecord> search(Long batchId, Long productId, Long operatorId, 
                                    Boolean isActive, Pageable pageable) {
        return eggRecordRepository.search(batchId, productId, operatorId, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<EggRecord> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return eggRecordRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return eggRecordRepository.countByIsActiveTrue();
    }
    
    public Integer sumCountByBatchId(Long batchId) {
        return eggRecordRepository.sumCountByBatchId(batchId);
    }
    
    public Integer sumCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return eggRecordRepository.sumCountByDateRange(startDate, endDate);
    }
    
    public Integer sumTotalCount() {
        return eggRecordRepository.sumTotalCount();
    }
    
    public BigDecimal sumTotalWeight() {
        return eggRecordRepository.sumTotalWeight();
    }
    
    // ========== 保存和删除 ==========
    
    public EggRecord save(EggRecord record) {
        if (record.getCreatedTime() == null) {
            record.setCreatedTime(LocalDateTime.now());
        }
        return eggRecordRepository.save(record);
    }
    
    public void deleteById(Long id) {
        eggRecordRepository.findById(id).ifPresent(record -> {
            record.setIsActive(false);
            eggRecordRepository.save(record);
        });
    }
}