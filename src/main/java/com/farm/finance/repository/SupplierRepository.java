package com.farm.finance.repository;

import com.farm.finance.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<Supplier> findBySupplierNo(String supplierNo);
    
    Optional<Supplier> findByName(String name);
    
    boolean existsBySupplierNo(String supplierNo);
    
    // ========== 按字段查询 ==========
    
    List<Supplier> findByContactPerson(String contactPerson);
    
    List<Supplier> findByContactPersonContaining(String contactPerson);
    
    Optional<Supplier> findByPhone(String phone);
    
    List<Supplier> findByPhoneContaining(String phone);
    
    List<Supplier> findByNameContaining(String name);
    
    List<Supplier> findByIsActive(Boolean isActive);
    
    List<Supplier> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<Supplier> findByNameContaining(String name, Pageable pageable);
    
    Page<Supplier> findByPhoneContaining(String phone, Pageable pageable);
    
    Page<Supplier> findByContactPersonContaining(String contactPerson, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true ORDER BY s.name")
    List<Supplier> findAllActive();
    
    @Query("SELECT s FROM Supplier s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Supplier> searchByName(@Param("keyword") String keyword);
    
    @Query("SELECT s FROM Supplier s WHERE " +
           "(:name IS NULL OR s.name LIKE %:name%) AND " +
           "(:contactPerson IS NULL OR s.contactPerson LIKE %:contactPerson%) AND " +
           "(:phone IS NULL OR s.phone LIKE %:phone%) AND " +
           "(:isActive IS NULL OR s.isActive = :isActive)")
    Page<Supplier> search(@Param("name") String name,
                          @Param("contactPerson") String contactPerson,
                          @Param("phone") String phone,
                          @Param("isActive") Boolean isActive,
                          Pageable pageable);
    
    @Query("SELECT s FROM Supplier s WHERE s.createdTime BETWEEN :startTime AND :endTime")
    List<Supplier> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    boolean existsByPhone(String phone);
    
    // ========== 关键词搜索 ==========
    
    @Query("SELECT s FROM Supplier s WHERE s.isActive = true AND " +
           "(LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.supplierNo) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(s.phone) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Supplier> searchByKeyword(@Param("keyword") String keyword);
}