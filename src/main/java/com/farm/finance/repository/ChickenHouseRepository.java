package com.farm.finance.repository;

import com.farm.finance.entity.ChickenHouse;
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
public interface ChickenHouseRepository extends JpaRepository<ChickenHouse, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<ChickenHouse> findByHouseNo(String houseNo);
    
    Optional<ChickenHouse> findByName(String name);
    
    boolean existsByHouseNo(String houseNo);
    
    boolean existsByName(String name);
    
    // ========== 按字段查询 ==========
    
    List<ChickenHouse> findByStatus(String status);
    
    List<ChickenHouse> findByCapacity(Integer capacity);
    
    List<ChickenHouse> findByCapacityBetween(Integer minCapacity, Integer maxCapacity);
    
    List<ChickenHouse> findByAreaBetween(BigDecimal minArea, BigDecimal maxArea);
    
    List<ChickenHouse> findByIsActive(Boolean isActive);
    
    List<ChickenHouse> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<ChickenHouse> findByStatus(String status, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT h FROM ChickenHouse h WHERE h.status = 'ACTIVE' AND h.isActive = true ORDER BY h.houseNo")
    List<ChickenHouse> findActiveHouses();
    
    @Query("SELECT SUM(h.capacity) FROM ChickenHouse h WHERE h.status = 'ACTIVE' AND h.isActive = true")
    Integer getTotalCapacity();
    
    @Query("SELECT h FROM ChickenHouse h WHERE " +
           "(:status IS NULL OR h.status = :status) AND " +
           "(:isActive IS NULL OR h.isActive = :isActive)")
    Page<ChickenHouse> search(@Param("status") String status,
                               @Param("isActive") Boolean isActive,
                               Pageable pageable);
    
    @Query("SELECT h FROM ChickenHouse h WHERE h.createdTime BETWEEN :startTime AND :endTime")
    List<ChickenHouse> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByStatus(String status);
    
    @Query("SELECT COUNT(h) FROM ChickenHouse h WHERE h.status = :status AND h.isActive = true")
    long countByStatusAndIsActiveTrue(@Param("status") String status);
    
    @Query("SELECT SUM(h.area) FROM ChickenHouse h WHERE h.isActive = true")
    BigDecimal getTotalArea();
}