package com.farm.finance.repository;

import com.farm.finance.entity.EggRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EggRecordRepository extends JpaRepository<EggRecord, Long> {
    
    // ========== 基本查询 ==========
    
    List<EggRecord> findByBatchId(Long batchId);
    
    List<EggRecord> findByProductId(Long productId);
    
    List<EggRecord> findByOperatorId(Long operatorId);
    
    List<EggRecord> findByIsActive(Boolean isActive);
    
    List<EggRecord> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<EggRecord> findByProductionDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<EggRecord> findByBatchId(Long batchId, Pageable pageable);
    
    Page<EggRecord> findByProductId(Long productId, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT e FROM EggRecord e WHERE e.batchId = :batchId ORDER BY e.productionDate DESC")
    List<EggRecord> findByBatchIdOrderByDateDesc(@Param("batchId") Long batchId);
    
    @Query("SELECT e FROM EggRecord e WHERE " +
           "(:batchId IS NULL OR e.batchId = :batchId) AND " +
           "(:productId IS NULL OR e.productId = :productId) AND " +
           "(:operatorId IS NULL OR e.operatorId = :operatorId) AND " +
           "(:isActive IS NULL OR e.isActive = :isActive)")
    Page<EggRecord> search(@Param("batchId") Long batchId,
                            @Param("productId") Long productId,
                            @Param("operatorId") Long operatorId,
                            @Param("isActive") Boolean isActive,
                            Pageable pageable);
    
    @Query("SELECT e FROM EggRecord e WHERE e.createdTime BETWEEN :startTime AND :endTime")
    List<EggRecord> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    @Query("SELECT SUM(e.totalCount) FROM EggRecord e WHERE e.batchId = :batchId")
    Integer sumCountByBatchId(@Param("batchId") Long batchId);
    
    @Query("SELECT SUM(e.totalCount) FROM EggRecord e WHERE e.productionDate BETWEEN :startDate AND :endDate")
    Integer sumCountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(e.totalCount) FROM EggRecord e WHERE e.isActive = true")
    Integer sumTotalCount();
    
    @Query("SELECT SUM(e.totalWeight) FROM EggRecord e WHERE e.isActive = true")
    java.math.BigDecimal sumTotalWeight();
}