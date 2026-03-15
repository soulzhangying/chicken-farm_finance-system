package com.farm.finance.repository;

import com.farm.finance.entity.FeedRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface FeedRecordRepository extends JpaRepository<FeedRecord, Long> {
    
    List<FeedRecord> findByBatchId(Long batchId);
    
    List<FeedRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<FeedRecord> findByProductId(Long productId);
    
    @Query("SELECT SUM(f.totalCost) FROM FeedRecord f WHERE f.batchId = :batchId")
    BigDecimal sumCostByBatchId(@Param("batchId") Long batchId);
    
    @Query("SELECT SUM(f.totalCost) FROM FeedRecord f WHERE f.recordDate BETWEEN :startDate AND :endDate")
    BigDecimal sumCostByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
