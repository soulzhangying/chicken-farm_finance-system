package com.farm.finance.repository;

import com.farm.finance.entity.SalesOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<SalesOrder> findByOrderNo(String orderNo);
    
    boolean existsByOrderNo(String orderNo);
    
    // ========== 按字段查询 ==========
    
    List<SalesOrder> findByCustomerId(Long customerId);
    
    List<SalesOrder> findByPaymentStatus(String paymentStatus);
    
    List<SalesOrder> findByOrderStatus(String orderStatus);
    
    List<SalesOrder> findByDeliveryType(String deliveryType);
    
    List<SalesOrder> findByIsActive(Boolean isActive);
    
    List<SalesOrder> findByIsActiveTrue();
    
    // ========== 日期范围查询 ==========
    
    List<SalesOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    // ========== 分页查询 ==========
    
    Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable);
    
    Page<SalesOrder> findByPaymentStatus(String paymentStatus, Pageable pageable);
    
    Page<SalesOrder> findByOrderStatus(String orderStatus, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT s FROM SalesOrder s WHERE " +
           "(:customerId IS NULL OR s.customerId = :customerId) AND " +
           "(:paymentStatus IS NULL OR s.paymentStatus = :paymentStatus) AND " +
           "(:orderStatus IS NULL OR s.orderStatus = :orderStatus) AND " +
           "(:isActive IS NULL OR s.isActive = :isActive)")
    Page<SalesOrder> search(@Param("customerId") Long customerId,
                             @Param("paymentStatus") String paymentStatus,
                             @Param("orderStatus") String orderStatus,
                             @Param("isActive") Boolean isActive,
                             Pageable pageable);
    
    @Query("SELECT s FROM SalesOrder s WHERE s.createdTime BETWEEN :startTime AND :endTime")
    List<SalesOrder> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                               @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByPaymentStatus(String paymentStatus);
    
    long countByOrderStatus(String orderStatus);
    
    @Query("SELECT SUM(s.totalAmount) FROM SalesOrder s WHERE s.isActive = true")
    BigDecimal sumTotalAmount();
    
    @Query("SELECT SUM(s.actualAmount) FROM SalesOrder s WHERE s.isActive = true")
    BigDecimal sumActualAmount();
    
    @Query("SELECT SUM(s.actualAmount) FROM SalesOrder s WHERE s.paymentStatus = :paymentStatus AND s.isActive = true")
    BigDecimal sumAmountByPaymentStatus(@Param("paymentStatus") String paymentStatus);
    
    // ========== 关键词搜索 ==========
    
    @Query("SELECT s FROM SalesOrder s WHERE s.isActive = true AND " +
           "(LOWER(s.orderNo) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<SalesOrder> searchByKeyword(@Param("keyword") String keyword);
}