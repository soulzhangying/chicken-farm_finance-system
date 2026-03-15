package com.farm.finance.repository;

import com.farm.finance.entity.FinanceRecord;
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
public interface FinanceRecordRepository extends JpaRepository<FinanceRecord, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<FinanceRecord> findByRecordNo(String recordNo);
    
    boolean existsByRecordNo(String recordNo);
    
    // ========== 按字段查询 ==========
    
    List<FinanceRecord> findByMoneyType(String moneyType);
    
    List<FinanceRecord> findByCostType(String costType);
    
    List<FinanceRecord> findByIncomeType(String incomeType);
    
    List<FinanceRecord> findByProductId(Long productId);
    
    List<FinanceRecord> findByBatchNo(String batchNo);
    
    List<FinanceRecord> findByCustomerId(Long customerId);
    
    List<FinanceRecord> findBySupplierId(Long supplierId);
    
    List<FinanceRecord> findByCreatedBy(Long createdBy);
    
    List<FinanceRecord> findByPaymentMethod(String paymentMethod);
    
    List<FinanceRecord> findByIsActive(Boolean isActive);
    
    List<FinanceRecord> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<FinanceRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<FinanceRecord> findByMoneyType(String moneyType, Pageable pageable);
    
    Page<FinanceRecord> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<FinanceRecord> findBySupplierId(Long supplierId, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT f FROM FinanceRecord f WHERE " +
           "(:moneyType IS NULL OR f.moneyType = :moneyType) AND " +
           "(:costType IS NULL OR f.costType = :costType) AND " +
           "(:incomeType IS NULL OR f.incomeType = :incomeType) AND " +
           "(:customerId IS NULL OR f.customerId = :customerId) AND " +
           "(:supplierId IS NULL OR f.supplierId = :supplierId) AND " +
           "(:isActive IS NULL OR f.isActive = :isActive)")
    Page<FinanceRecord> search(@Param("moneyType") String moneyType,
                                @Param("costType") String costType,
                                @Param("incomeType") String incomeType,
                                @Param("customerId") Long customerId,
                                @Param("supplierId") Long supplierId,
                                @Param("isActive") Boolean isActive,
                                Pageable pageable);
    
    @Query("SELECT f FROM FinanceRecord f WHERE f.createdTime BETWEEN :startTime AND :endTime")
    List<FinanceRecord> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.moneyType = 'INCOME' AND f.recordDate BETWEEN :startDate AND :endDate")
    BigDecimal sumIncomeByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.moneyType = 'COST' AND f.recordDate BETWEEN :startDate AND :endDate")
    BigDecimal sumCostByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.moneyType = 'INCOME' AND f.isActive = true")
    BigDecimal sumTotalIncome();
    
    @Query("SELECT SUM(f.amount) FROM FinanceRecord f WHERE f.moneyType = 'COST' AND f.isActive = true")
    BigDecimal sumTotalCost();
}