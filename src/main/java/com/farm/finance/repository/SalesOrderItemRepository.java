package com.farm.finance.repository;

import com.farm.finance.entity.SalesOrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SalesOrderItemRepository extends JpaRepository<SalesOrderItem, Long> {
    
    // ========== 按字段查询 ==========
    
    List<SalesOrderItem> findByOrderId(Long orderId);
    
    List<SalesOrderItem> findByProductId(Long productId);
    
    List<SalesOrderItem> findByBatchNo(String batchNo);
    
    List<SalesOrderItem> findByUnit(String unit);
    
    List<SalesOrderItem> findByIsActive(Boolean isActive);
    
    List<SalesOrderItem> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<SalesOrderItem> findByOrderId(Long orderId, Pageable pageable);
    
    Page<SalesOrderItem> findByProductId(Long productId, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT i FROM SalesOrderItem i WHERE " +
           "(:orderId IS NULL OR i.orderId = :orderId) AND " +
           "(:productId IS NULL OR i.productId = :productId) AND " +
           "(:isActive IS NULL OR i.isActive = :isActive)")
    Page<SalesOrderItem> search(@Param("orderId") Long orderId,
                                 @Param("productId") Long productId,
                                 @Param("isActive") Boolean isActive,
                                 Pageable pageable);
    
    @Query("SELECT i FROM SalesOrderItem i WHERE i.createdTime BETWEEN :startTime AND :endTime")
    List<SalesOrderItem> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                    @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByOrderId(Long orderId);
    
    @Query("SELECT SUM(i.totalPrice) FROM SalesOrderItem i WHERE i.orderId = :orderId AND i.isActive = true")
    BigDecimal sumTotalPriceByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(i.quantity) FROM SalesOrderItem i WHERE i.orderId = :orderId AND i.isActive = true")
    BigDecimal sumQuantityByOrderId(@Param("orderId") Long orderId);
}