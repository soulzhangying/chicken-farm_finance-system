package com.farm.finance.repository;

import com.farm.finance.entity.InventoryTransaction;
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
public interface InventoryTransactionRepository extends JpaRepository<InventoryTransaction, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<InventoryTransaction> findByTransactionNo(String transactionNo);
    
    boolean existsByTransactionNo(String transactionNo);
    
    // ========== 按字段查询 ==========
    
    List<InventoryTransaction> findByTransactionType(String transactionType);
    
    List<InventoryTransaction> findByProductId(Long productId);
    
    List<InventoryTransaction> findByBatchNo(String batchNo);
    
    List<InventoryTransaction> findByOperatorId(Long operatorId);
    
    List<InventoryTransaction> findByRelatedMoneyId(Long relatedMoneyId);
    
    List<InventoryTransaction> findByIsActive(Boolean isActive);
    
    List<InventoryTransaction> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<InventoryTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<InventoryTransaction> findByProductId(Long productId, Pageable pageable);
    
    Page<InventoryTransaction> findByTransactionType(String transactionType, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.productId = :productId ORDER BY t.transactionDate DESC")
    List<InventoryTransaction> findByProductIdOrderByDateDesc(@Param("productId") Long productId);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE " +
           "(:transactionType IS NULL OR t.transactionType = :transactionType) AND " +
           "(:productId IS NULL OR t.productId = :productId) AND " +
           "(:operatorId IS NULL OR t.operatorId = :operatorId) AND " +
           "(:isActive IS NULL OR t.isActive = :isActive)")
    Page<InventoryTransaction> search(@Param("transactionType") String transactionType,
                                       @Param("productId") Long productId,
                                       @Param("operatorId") Long operatorId,
                                       @Param("isActive") Boolean isActive,
                                       Pageable pageable);
    
    @Query("SELECT t FROM InventoryTransaction t WHERE t.createdTime BETWEEN :startTime AND :endTime")
    List<InventoryTransaction> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                         @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    @Query("SELECT SUM(t.quantity) FROM InventoryTransaction t WHERE t.transactionType = :transactionType AND t.isActive = true")
    BigDecimal sumQuantityByTransactionType(@Param("transactionType") String transactionType);
    
    @Query("SELECT SUM(t.totalAmount) FROM InventoryTransaction t WHERE t.transactionType = :transactionType AND t.isActive = true")
    BigDecimal sumAmountByTransactionType(@Param("transactionType") String transactionType);
}