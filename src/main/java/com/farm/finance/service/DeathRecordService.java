package com.farm.finance.service;

import com.farm.finance.entity.DeathRecord;
import com.farm.finance.repository.DeathRecordRepository;
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
public class DeathRecordService {
    
    private final DeathRecordRepository deathRecordRepository;
    
    // ========== 基本查询 ==========
    
    public List<DeathRecord> findAll() {
        return deathRecordRepository.findAll();
    }
    
    public Page<DeathRecord> findAll(Pageable pageable) {
        return deathRecordRepository.findAll(pageable);
    }
    
    public Optional<DeathRecord> findById(Long id) {
        return deathRecordRepository.findById(id);
    }
    
    // ========== 按字段查询 ==========
    
    public List<DeathRecord> findByHouseId(Long houseId) {
        return deathRecordRepository.findByHouseId(houseId);
    }
    
    public List<DeathRecord> findByOperatorId(Long operatorId) {
        return deathRecordRepository.findByOperatorId(operatorId);
    }
    
    public List<DeathRecord> findByDeathReason(String deathReason) {
        return deathRecordRepository.findByDeathReason(deathReason);
    }
    
    public List<DeathRecord> findByIsActive(Boolean isActive) {
        return deathRecordRepository.findByIsActive(isActive);
    }
    
    public List<DeathRecord> findByIsActiveTrue() {
        return deathRecordRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<DeathRecord> findByDeathDateBetween(LocalDate startDate, LocalDate endDate) {
        return deathRecordRepository.findByDeathDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<DeathRecord> findByHouseId(Long houseId, Pageable pageable) {
        return deathRecordRepository.findByHouseId(houseId, pageable);
    }
    
    public Page<DeathRecord> findByOperatorId(Long operatorId, Pageable pageable) {
        return deathRecordRepository.findByOperatorId(operatorId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<DeathRecord> search(Long houseId, Long operatorId, String deathReason, 
                                      Boolean isActive, Pageable pageable) {
        return deathRecordRepository.search(houseId, operatorId, deathReason, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<DeathRecord> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return deathRecordRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return deathRecordRepository.countByIsActiveTrue();
    }
    
    public Integer sumDeathCountByHouseId(Long houseId) {
        return deathRecordRepository.sumDeathCountByHouseId(houseId);
    }
    
    public Integer sumDeathCountByDateRange(LocalDate startDate, LocalDate endDate) {
        return deathRecordRepository.sumDeathCountByDateRange(startDate, endDate);
    }
    
    public Integer sumTotalDeathCount() {
        return deathRecordRepository.sumTotalDeathCount();
    }
    
    public List<Object[]> sumDeathCountGroupByReason() {
        return deathRecordRepository.sumDeathCountGroupByReason();
    }
    
    // ========== 保存和删除 ==========
    
    public DeathRecord save(DeathRecord record) {
        if (record.getCreatedTime() == null) {
            record.setCreatedTime(LocalDateTime.now());
        }
        return deathRecordRepository.save(record);
    }
    
    public void deleteById(Long id) {
        deathRecordRepository.findById(id).ifPresent(record -> {
            record.setIsActive(false);
            deathRecordRepository.save(record);
        });
    }
}
