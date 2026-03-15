package com.farm.finance.service;

import com.farm.finance.entity.FeedRecord;
import com.farm.finance.repository.FeedRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FeedRecordService {
    
    private final FeedRecordRepository feedRecordRepository;
    
    public List<FeedRecord> findAll() {
        return feedRecordRepository.findAll();
    }
    
    public Optional<FeedRecord> findById(Long id) {
        return feedRecordRepository.findById(id);
    }
    
    public List<FeedRecord> findByBatchId(Long batchId) {
        return feedRecordRepository.findByBatchId(batchId);
    }
    
    public List<FeedRecord> findByProductId(Long productId) {
        return feedRecordRepository.findByProductId(productId);
    }
    
    public BigDecimal sumCostByBatchId(Long batchId) {
        return feedRecordRepository.sumCostByBatchId(batchId);
    }
    
    public BigDecimal sumCostByDateRange(LocalDate startDate, LocalDate endDate) {
        return feedRecordRepository.sumCostByDateRange(startDate, endDate);
    }
    
    public FeedRecord save(FeedRecord record) {
        if (record.getCreatedTime() == null) {
            record.setCreatedTime(java.time.LocalDateTime.now());
        }
        return feedRecordRepository.save(record);
    }
    
    public void deleteById(Long id) {
        feedRecordRepository.findById(id).ifPresent(record -> {
            record.setIsActive(false);
            feedRecordRepository.save(record);
        });
    }
}
