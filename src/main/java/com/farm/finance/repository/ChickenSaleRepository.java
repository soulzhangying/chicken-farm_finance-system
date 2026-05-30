package com.farm.finance.repository;

import com.farm.finance.entity.ChickenSale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChickenSaleRepository extends JpaRepository<ChickenSale, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<ChickenSale> findBySaleNo(String saleNo);
    
    boolean existsBySaleNo(String saleNo);
    
    // ========== 按字段查询 ==========
    
    List<ChickenSale> findByBatchId(Long batchId);
    
    List<ChickenSale> findByCustomerId(Long customerId);
    
    List<ChickenSale> findBySaleDate(LocalDate saleDate);
    
    List<ChickenSale> findByIsActive(Boolean isActive);
    
    List<ChickenSale> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<ChickenSale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<ChickenSale> findByBatchId(Long batchId, Pageable pageable);
    
    Page<ChickenSale> findByCustomerId(Long customerId, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT c FROM ChickenSale c WHERE " +
           "(:batchId IS NULL OR c.batchId = :batchId) AND " +
           "(:customerId IS NULL OR c.customerId = :customerId) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<ChickenSale> search(@Param("batchId") Long batchId,
                              @Param("customerId") Long customerId,
                              @Param("isActive") Boolean isActive,
                              Pageable pageable);
    
    @Query("SELECT c FROM ChickenSale c WHERE c.createdTime BETWEEN :startTime AND :endTime")
    List<ChickenSale> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByBatchId(Long batchId);
    
    @Query("SELECT SUM(c.totalAmount) FROM ChickenSale c WHERE c.isActive = true")
    BigDecimal sumTotalAmount();
    
    @Query("SELECT SUM(c.quantity) FROM ChickenSale c WHERE c.batchId = :batchId AND c.isActive = true")
    Integer sumQuantityByBatchId(@Param("batchId") Long batchId);
    
    @Query("SELECT SUM(c.weight) FROM ChickenSale c WHERE c.batchId = :batchId AND c.isActive = true")
    BigDecimal sumWeightByBatchId(@Param("batchId") Long batchId);
    
    @Query("SELECT SUM(c.totalAmount) FROM ChickenSale c WHERE c.batchId = :batchId AND c.isActive = true")
    BigDecimal sumTotalAmountByBatchId(@Param("batchId") Long batchId);
    
    // ========== 关键词和日期范围搜索 ==========
    
    @Query("SELECT c FROM ChickenSale c JOIN ChickenBatch b ON c.batchId = b.id " +
           "WHERE c.isActive = true AND " +
           "(LOWER(b.batchName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(b.batchNo) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<ChickenSale> searchByKeyword(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM ChickenSale c WHERE c.isActive = true AND " +
           "(:batchId IS NULL OR c.batchId = :batchId) AND " +
           "(:startDate IS NULL OR c.saleDate >= :startDate) AND " +
           "(:endDate IS NULL OR c.saleDate <= :endDate)")
    List<ChickenSale> searchByBatchIdAndDateRange(@Param("batchId") Long batchId,
                                                   @Param("startDate") LocalDate startDate,
                                                   @Param("endDate") LocalDate endDate);
    
    // ========== 按批次汇总销售额 ==========
    
    @Query("SELECT c.batchId, SUM(c.quantity), SUM(c.weight), SUM(c.totalAmount), COUNT(c) " +
           "FROM ChickenSale c WHERE c.isActive = true " +
           "GROUP BY c.batchId ORDER BY SUM(c.totalAmount) DESC")
    List<Object[]> sumByBatchId();
    
    @Query("SELECT c.batchId, SUM(c.quantity), SUM(c.weight), SUM(c.totalAmount), COUNT(c) " +
           "FROM ChickenSale c WHERE c.isActive = true " +
           "AND (:startDate IS NULL OR c.saleDate >= :startDate) " +
           "AND (:endDate IS NULL OR c.saleDate <= :endDate) " +
           "GROUP BY c.batchId ORDER BY SUM(c.totalAmount) DESC")
    List<Object[]> sumByBatchIdAndDateRange(@Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);
}
