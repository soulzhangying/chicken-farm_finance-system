package com.farm.finance.repository;

import com.farm.finance.entity.Product;
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
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<Product> findByProductNo(String productNo);
    
    Optional<Product> findByName(String name);
    
    boolean existsByProductNo(String productNo);
    
    boolean existsByName(String name);
    
    // ========== 按字段查询 ==========
    
    List<Product> findByProductType(String productType);
    
    List<Product> findByProductTypeAndIsActiveTrue(String productType);
    
    List<Product> findByUnit(String unit);
    
    List<Product> findByIsActive(Boolean isActive);
    
    List<Product> findByIsActiveTrue();
    
    // ========== 价格范围查询 ==========
    
    List<Product> findBySalePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    List<Product> findByPurchasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    
    // ========== 分页查询 ==========
    
    Page<Product> findByProductType(String productType, Pageable pageable);
    
    Page<Product> findByUnit(String unit, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.name")
    List<Product> findAllActive();
    
    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.productType = :type ORDER BY p.name")
    List<Product> findActiveByType(@Param("type") String type);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:productType IS NULL OR p.productType = :productType) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<Product> search(@Param("productType") String productType,
                         @Param("isActive") Boolean isActive,
                         Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE " +
           "(:productType IS NULL OR p.productType = :productType) AND " +
           "(:minPrice IS NULL OR p.salePrice >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.salePrice <= :maxPrice) AND " +
           "(:isActive IS NULL OR p.isActive = :isActive)")
    Page<Product> searchWithPrice(@Param("productType") String productType,
                                  @Param("minPrice") BigDecimal minPrice,
                                  @Param("maxPrice") BigDecimal maxPrice,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);
    
    @Query("SELECT p FROM Product p WHERE p.createdTime BETWEEN :startTime AND :endTime")
    List<Product> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByProductType(String productType);
    
    @Query("SELECT COUNT(p) FROM Product p WHERE p.productType = :productType AND p.isActive = true")
    long countByProductTypeAndIsActiveTrue(@Param("productType") String productType);
    
    @Query("SELECT p.productType, COUNT(p) FROM Product p WHERE p.isActive = true GROUP BY p.productType")
    List<Object[]> countGroupByProductType();
}
