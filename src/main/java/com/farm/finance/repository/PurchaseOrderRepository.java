package com.farm.finance.repository;

import com.farm.finance.entity.PurchaseOrder;
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
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<PurchaseOrder> findByOrderNo(String orderNo);
    
    boolean existsByOrderNo(String orderNo);
    
    // ========== 按字段查询 ==========
    
    List<PurchaseOrder> findBySupplierId(Long supplierId);
    
    List<PurchaseOrder> findByCreatedBy(Long createdBy);
    
    List<PurchaseOrder> findByPaymentStatus(String paymentStatus);
    
    List<PurchaseOrder> findByDeliveryStatus(String deliveryStatus);
    
    List<PurchaseOrder> findByIsActive(Boolean isActive);
    
    List<PurchaseOrder> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate);
    
    // ========== 分页查询 ==========
    
    Page<PurchaseOrder> findBySupplierId(Long supplierId, Pageable pageable);
    
    Page<PurchaseOrder> findByPaymentStatus(String paymentStatus, Pageable pageable);
    
    Page<PurchaseOrder> findByDeliveryStatus(String deliveryStatus, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT p FROM PurchaseOrder p WHERE " +
           "(:supplierId IS NULL OR p.supplierId = :supplierId) AND " +
           "(:paymentStatus IS NULL OR p.paymentStatus = :paymentStatus) AND " +
           "(:deliveryStatus IS NULL OR p.deliveryStatus = :deliveryStatus) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<PurchaseOrder> search(@Param("supplierId") Long supplierId,
                                @Param("paymentStatus") String paymentStatus,
                                @Param("deliveryStatus") String deliveryStatus,
                                @Param("isActive") Boolean isActive,
                                Pageable pageable);
    
    @Query("SELECT p FROM PurchaseOrder p WHERE p.createdTime BETWEEN :startTime AND :endTime")
    List<PurchaseOrder> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                   @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByPaymentStatus(String paymentStatus);
    
    long countByDeliveryStatus(String deliveryStatus);
    
    @Query("SELECT SUM(p.totalAmount) FROM PurchaseOrder p WHERE p.isActive = true")
    BigDecimal sumTotalAmount();
    
    @Query("SELECT SUM(p.totalAmount) FROM PurchaseOrder p WHERE p.paymentStatus = :paymentStatus AND p.isActive = true")
    BigDecimal sumAmountByPaymentStatus(@Param("paymentStatus") String paymentStatus);
    
    // ========== 关键词搜索 ==========
    
    @Query("SELECT p FROM PurchaseOrder p WHERE p.isActive = true AND " +
           "(LOWER(p.orderNo) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<PurchaseOrder> searchByKeyword(@Param("keyword") String keyword);
}