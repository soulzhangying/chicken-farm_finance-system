package com.farm.finance.repository;

import com.farm.finance.entity.PurchaseOrderItem;
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
public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItem, Long> {
    
    // ========== 按字段查询 ==========
    
    List<PurchaseOrderItem> findByOrderId(Long orderId);
    
    List<PurchaseOrderItem> findByProductId(Long productId);
    
    List<PurchaseOrderItem> findByUnit(String unit);
    
    List<PurchaseOrderItem> findByIsActive(Boolean isActive);
    
    List<PurchaseOrderItem> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<PurchaseOrderItem> findByOrderId(Long orderId, Pageable pageable);
    
    Page<PurchaseOrderItem> findByProductId(Long productId, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT i FROM PurchaseOrderItem i WHERE " +
           "(:orderId IS NULL OR i.orderId = :orderId) AND " +
           "(:productId IS NULL OR i.productId = :productId) AND " +
           "(:isActive IS NULL OR i.isActive = :isActive)")
    Page<PurchaseOrderItem> search(@Param("orderId") Long orderId,
                                    @Param("productId") Long productId,
                                    @Param("isActive") Boolean isActive,
                                    Pageable pageable);
    
    @Query("SELECT i FROM PurchaseOrderItem i WHERE i.createdTime BETWEEN :startTime AND :endTime")
    List<PurchaseOrderItem> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                       @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByOrderId(Long orderId);
    
    @Query("SELECT SUM(i.totalPrice) FROM PurchaseOrderItem i WHERE i.orderId = :orderId AND i.isActive = true")
    BigDecimal sumTotalPriceByOrderId(@Param("orderId") Long orderId);
    
    @Query("SELECT SUM(i.quantity) FROM PurchaseOrderItem i WHERE i.orderId = :orderId AND i.isActive = true")
    BigDecimal sumQuantityByOrderId(@Param("orderId") Long orderId);
}