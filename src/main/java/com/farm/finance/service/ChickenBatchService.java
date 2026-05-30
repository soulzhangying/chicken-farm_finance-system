package com.farm.finance.service;

import com.farm.finance.entity.ChickenBatch;
import com.farm.finance.repository.ChickenBatchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChickenBatchService {
    
    private final ChickenBatchRepository chickenBatchRepository;
    
    public List<ChickenBatch> findAll() {
        return chickenBatchRepository.findAll();
    }
    
    public List<ChickenBatch> findAllActive() {
        return chickenBatchRepository.findByIsActiveTrueOrderByCreatedTimeDesc();
    }
    
    public Optional<ChickenBatch> findById(Long id) {
        return chickenBatchRepository.findByIdAndIsActiveTrue(id);
    }
    
    public Optional<ChickenBatch> findByBatchNo(String batchNo) {
        return chickenBatchRepository.findByBatchNo(batchNo);
    }
    
    public List<ChickenBatch> findByHouseId(Long houseId) {
        return chickenBatchRepository.findByHouseIdAndIsActiveTrue(houseId);
    }
    
    public List<ChickenBatch> findByStatus(String status) {
        return chickenBatchRepository.findByStatus(status);
    }
    
    public List<ChickenBatch> searchByKeyword(String keyword) {
        return chickenBatchRepository.searchByKeyword(keyword);
    }
    
    public Page<ChickenBatch> search(Long houseId, String status, Pageable pageable) {
        return chickenBatchRepository.search(houseId, status, pageable);
    }
    
    @Transactional
    public ChickenBatch save(ChickenBatch batch) {
        if (batch.getId() == null) {
            batch.setCreatedTime(LocalDateTime.now());
            if (batch.getIsActive() == null) {
                batch.setIsActive(true);
            }
            if (batch.getStatus() == null) {
                batch.setStatus("ACTIVE");
            }
            if (batch.getTotalDeathCount() == null) {
                batch.setTotalDeathCount(0);
            }
            if (batch.getCurrentAge() == null) {
                batch.setCurrentAge(0);
            }
            // 如果没有设置当前数量，默认为入场数量
            if (batch.getCurrentQuantity() == null && batch.getEntryQuantity() != null) {
                batch.setCurrentQuantity(batch.getEntryQuantity());
            }
        }
        return chickenBatchRepository.save(batch);
    }
    
    @Transactional
    public void deleteById(Long id) {
        chickenBatchRepository.findById(id).ifPresent(batch -> {
            batch.setIsActive(false);
            chickenBatchRepository.save(batch);
        });
    }
    
    public boolean existsByBatchNo(String batchNo) {
        return chickenBatchRepository.existsByBatchNo(batchNo);
    }
    
    public boolean existsByBatchName(String batchName) {
        return chickenBatchRepository.existsByBatchName(batchName);
    }
    
    public Integer sumEntryQuantity() {
        Integer sum = chickenBatchRepository.sumEntryQuantity();
        return sum != null ? sum : 0;
    }
    
    public Integer sumCurrentQuantity() {
        Integer sum = chickenBatchRepository.sumCurrentQuantity();
        return sum != null ? sum : 0;
    }
}
