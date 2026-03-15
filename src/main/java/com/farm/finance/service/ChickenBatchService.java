package com.farm.finance.service;

import com.farm.finance.entity.ChickenBatch;
import com.farm.finance.repository.ChickenBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ChickenBatchService {
    
    private final ChickenBatchRepository chickenBatchRepository;
    
    // ========== 基本查询 ==========
    
    public List<ChickenBatch> findAll() {
        return chickenBatchRepository.findAll();
    }
    
    public Page<ChickenBatch> findAll(Pageable pageable) {
        return chickenBatchRepository.findAll(pageable);
    }
    
    public Optional<ChickenBatch> findById(Long id) {
        return chickenBatchRepository.findById(id);
    }
    
    public Optional<ChickenBatch> findByBatchNo(String batchNo) {
        return chickenBatchRepository.findByBatchNo(batchNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<ChickenBatch> findByHouseId(Long houseId) {
        return chickenBatchRepository.findByHouseId(houseId);
    }
    
    public List<ChickenBatch> findBySupplierId(Long supplierId) {
        return chickenBatchRepository.findBySupplierId(supplierId);
    }
    
    public List<ChickenBatch> findByGroupName(String groupName) {
        return chickenBatchRepository.findByGroupName(groupName);
    }
    
    public List<ChickenBatch> findByBreed(String breed) {
        return chickenBatchRepository.findByBreed(breed);
    }
    
    public List<ChickenBatch> findByStatus(String status) {
        return chickenBatchRepository.findByStatus(status);
    }
    
    public List<ChickenBatch> findByIsActive(Boolean isActive) {
        return chickenBatchRepository.findByIsActive(isActive);
    }
    
    public List<ChickenBatch> findByIsActiveTrue() {
        return chickenBatchRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<ChickenBatch> findByEntryDateBetween(LocalDate startDate, LocalDate endDate) {
        return chickenBatchRepository.findByEntryDateBetween(startDate, endDate);
    }
    
    public List<ChickenBatch> findByExpectedSaleDateBetween(LocalDate startDate, LocalDate endDate) {
        return chickenBatchRepository.findByExpectedSaleDateBetween(startDate, endDate);
    }
    
    public List<ChickenBatch> findByActualSaleDateBetween(LocalDate startDate, LocalDate endDate) {
        return chickenBatchRepository.findByActualSaleDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<ChickenBatch> findByHouseId(Long houseId, Pageable pageable) {
        return chickenBatchRepository.findByHouseId(houseId, pageable);
    }
    
    public Page<ChickenBatch> findByStatus(String status, Pageable pageable) {
        return chickenBatchRepository.findByStatus(status, pageable);
    }
    
    // ========== 自定义查询 ==========
    
    public List<ChickenBatch> findActiveBatches() {
        return chickenBatchRepository.findActiveBatches();
    }
    
    public Integer getTotalActiveQuantity() {
        return chickenBatchRepository.getTotalActiveQuantity();
    }
    
    public List<ChickenBatch> findBatchesReadyForSale(LocalDate date) {
        return chickenBatchRepository.findBatchesReadyForSale(date);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<ChickenBatch> search(Long houseId, Long supplierId, String status, 
                                      Boolean isActive, Pageable pageable) {
        return chickenBatchRepository.search(houseId, supplierId, status, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<ChickenBatch> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return chickenBatchRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return chickenBatchRepository.countByIsActiveTrue();
    }
    
    public long countByStatus(String status) {
        return chickenBatchRepository.countByStatus(status);
    }
    
    public long countByStatusAndIsActiveTrue(String status) {
        return chickenBatchRepository.countByStatusAndIsActiveTrue(status);
    }
    
    public Integer getTotalEntryQuantity() {
        return chickenBatchRepository.getTotalEntryQuantity();
    }
    
    public Integer getTotalCurrentQuantity() {
        return chickenBatchRepository.getTotalCurrentQuantity();
    }
    
    public Integer getTotalDeathCount() {
        return chickenBatchRepository.getTotalDeathCount();
    }
    
    // ========== 保存和删除 ==========
    
    public ChickenBatch save(ChickenBatch batch) {
        if (batch.getCreatedTime() == null) {
            batch.setCreatedTime(LocalDateTime.now());
        }
        return chickenBatchRepository.save(batch);
    }
    
    public void deleteById(Long id) {
        chickenBatchRepository.findById(id).ifPresent(batch -> {
            batch.setIsActive(false);
            chickenBatchRepository.save(batch);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByBatchNo(String batchNo) {
        return chickenBatchRepository.existsByBatchNo(batchNo);
    }
}