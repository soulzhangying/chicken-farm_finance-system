package com.farm.finance.repository;

import com.farm.finance.entity.ChickenBatch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChickenBatchRepository extends JpaRepository<ChickenBatch, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<ChickenBatch> findByBatchNo(String batchNo);
    
    boolean existsByBatchNo(String batchNo);
    
    // ========== 按字段查询 ==========
    
    List<ChickenBatch> findByHouseId(Long houseId);
    
    List<ChickenBatch> findBySupplierId(Long supplierId);
    
    List<ChickenBatch> findByGroupName(String groupName);
    
    List<ChickenBatch> findByBreed(String breed);
    
    List<ChickenBatch> findByStatus(String status);
    
    List<ChickenBatch> findByIsActive(Boolean isActive);
    
    List<ChickenBatch> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<ChickenBatch> findByEntryDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ChickenBatch> findByExpectedSaleDateBetween(LocalDate startDate, LocalDate endDate);
    
    List<ChickenBatch> findByActualSaleDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<ChickenBatch> findByHouseId(Long houseId, Pageable pageable);
    
    Page<ChickenBatch> findByStatus(String status, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT b FROM ChickenBatch b WHERE b.status = 'ACTIVE' AND b.isActive = true ORDER BY b.entryDate DESC")
    List<ChickenBatch> findActiveBatches();
    
    @Query("SELECT SUM(b.currentQuantity) FROM ChickenBatch b WHERE b.status = 'ACTIVE' AND b.isActive = true")
    Integer getTotalActiveQuantity();
    
    @Query("SELECT b FROM ChickenBatch b WHERE b.expectedSaleDate <= :date AND b.status = 'ACTIVE' AND b.isActive = true")
    List<ChickenBatch> findBatchesReadyForSale(@Param("date") LocalDate date);
    
    @Query("SELECT b FROM ChickenBatch b WHERE " +
           "(:houseId IS NULL OR b.houseId = :houseId) AND " +
           "(:supplierId IS NULL OR b.supplierId = :supplierId) AND " +
           "(:status IS NULL OR b.status = :status) AND " +
           "(:isActive IS NULL OR b.isActive = :isActive)")
    Page<ChickenBatch> search(@Param("houseId") Long houseId,
                              @Param("supplierId") Long supplierId,
                              @Param("status") String status,
                              @Param("isActive") Boolean isActive,
                              Pageable pageable);
    
    @Query("SELECT b FROM ChickenBatch b WHERE b.createdTime BETWEEN :startTime AND :endTime")
    List<ChickenBatch> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByStatus(String status);
    
    @Query("SELECT COUNT(b) FROM ChickenBatch b WHERE b.status = :status AND b.isActive = true")
    long countByStatusAndIsActiveTrue(@Param("status") String status);
    
    @Query("SELECT SUM(b.entryQuantity) FROM ChickenBatch b WHERE b.isActive = true")
    Integer getTotalEntryQuantity();
    
    @Query("SELECT SUM(b.currentQuantity) FROM ChickenBatch b WHERE b.isActive = true")
    Integer getTotalCurrentQuantity();
    
    @Query("SELECT SUM(b.totalDeathCount) FROM ChickenBatch b WHERE b.isActive = true")
    Integer getTotalDeathCount();
}