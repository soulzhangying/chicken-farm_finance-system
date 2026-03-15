package com.farm.finance.repository;

import com.farm.finance.entity.EggSale;
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
public interface EggSaleRepository extends JpaRepository<EggSale, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<EggSale> findBySaleNo(String saleNo);
    
    boolean existsBySaleNo(String saleNo);
    
    // ========== 按字段查询 ==========
    
    List<EggSale> findByProductId(Long productId);
    
    List<EggSale> findByCustomerId(Long customerId);
    
    List<EggSale> findByCreatedBy(Long createdBy);
    
    List<EggSale> findByPaymentMethod(String paymentMethod);
    
    List<EggSale> findByIsActive(Boolean isActive);
    
    List<EggSale> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<EggSale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<EggSale> findByProductId(Long productId, Pageable pageable);
    
    Page<EggSale> findByCustomerId(Long customerId, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT s FROM EggSale s WHERE " +
           "(:productId IS NULL OR s.productId = :productId) AND " +
           "(:customerId IS NULL OR s.customerId = :customerId) AND " +
           "(:paymentMethod IS NULL OR s.paymentMethod = :paymentMethod) AND " +
           "(:isActive IS NULL OR s.isActive = :isActive)")
    Page<EggSale> search(@Param("productId") Long productId,
                          @Param("customerId") Long customerId,
                          @Param("paymentMethod") String paymentMethod,
                          @Param("isActive") Boolean isActive,
                          Pageable pageable);
    
    @Query("SELECT s FROM EggSale s WHERE s.createdTime BETWEEN :startTime AND :endTime")
    List<EggSale> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    @Query("SELECT SUM(s.totalAmount) FROM EggSale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    BigDecimal sumAmountByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(s.saleQuantity) FROM EggSale s WHERE s.saleDate BETWEEN :startDate AND :endDate")
    Integer sumQuantityByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT SUM(s.totalAmount) FROM EggSale s WHERE s.isActive = true")
    BigDecimal sumTotalAmount();
    
    @Query("SELECT SUM(s.saleQuantity) FROM EggSale s WHERE s.isActive = true")
    Integer sumTotalQuantity();
}