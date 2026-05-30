package com.farm.finance.repository;

import com.farm.finance.entity.Inventory;
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
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    
    // ========== 基本查询 ==========
    
    List<Inventory> findByProductId(Long productId);
    
    List<Inventory> findByHouseId(Long houseId);
    
    List<Inventory> findByBatchId(Long batchId);
    
    List<Inventory> findByProductIdAndBatchId(Long productId, Long batchId);
    
    List<Inventory> findByProductIdAndHouseId(Long productId, Long houseId);
    
    List<Inventory> findByUnit(String unit);
    
    List<Inventory> findByLocation(String location);
    
    List<Inventory> findByStatus(Boolean status);
    
    List<Inventory> findByIsActive(Boolean isActive);
    
    List<Inventory> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<Inventory> findByProductId(Long productId, Pageable pageable);
    
    Page<Inventory> findByStatus(Boolean status, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT i FROM Inventory i WHERE i.productId = :productId AND i.status = true")
    List<Inventory> findActiveByProductId(@Param("productId") Long productId);
    
    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.productId = :productId AND i.status = true")
    BigDecimal sumQuantityByProductId(@Param("productId") Long productId);
    
    @Query("SELECT i FROM Inventory i WHERE " +
           "(:productId IS NULL OR i.productId = :productId) AND " +
           "(:houseId IS NULL OR i.houseId = :houseId) AND " +
           "(:batchId IS NULL OR i.batchId = :batchId) AND " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:isActive IS NULL OR i.isActive = :isActive)")
    Page<Inventory> search(@Param("productId") Long productId,
                            @Param("houseId") Long houseId,
                            @Param("batchId") Long batchId,
                            @Param("status") Boolean status,
                            @Param("isActive") Boolean isActive,
                            Pageable pageable);
    
    @Query("SELECT i FROM Inventory i WHERE i.createdTime BETWEEN :startTime AND :endTime")
    List<Inventory> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                              @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByStatusTrue();
    
    @Query("SELECT SUM(i.quantity) FROM Inventory i WHERE i.status = true AND i.isActive = true")
    BigDecimal sumTotalQuantity();
    
    // ========== 关键词搜索 ==========
    
    @Query("SELECT i FROM Inventory i JOIN ChickenHouse h ON i.houseId = h.id WHERE i.isActive = true AND " +
           "(LOWER(h.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(h.houseNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(i.location) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Inventory> searchByKeyword(@Param("keyword") String keyword);
}