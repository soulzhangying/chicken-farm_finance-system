package com.farm.finance.repository;

import com.farm.finance.entity.Customer;
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
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<Customer> findByCustomerNo(String customerNo);
    
    Optional<Customer> findByName(String name);
    
    boolean existsByCustomerNo(String customerNo);
    
    // ========== 按字段查询 ==========
    
    List<Customer> findByPhone(String phone);
    
    List<Customer> findByPhoneContaining(String phone);
    
    List<Customer> findByAddressContaining(String address);
    
    List<Customer> findByIsActive(Boolean isActive);
    
    List<Customer> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<Customer> findByNameContaining(String name, Pageable pageable);
    
    Page<Customer> findByPhoneContaining(String phone, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT c FROM Customer c WHERE c.isActive = true ORDER BY c.name")
    List<Customer> findAllActive();
    
    @Query("SELECT c FROM Customer c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Customer> searchByName(@Param("keyword") String keyword);
    
    @Query("SELECT c FROM Customer c WHERE " +
           "(:name IS NULL OR c.name LIKE %:name%) AND " +
           "(:phone IS NULL OR c.phone LIKE %:phone%) AND " +
           "(:address IS NULL OR c.address LIKE %:address%) AND " +
           "(:isActive IS NULL OR c.isActive = :isActive)")
    Page<Customer> search(@Param("name") String name,
                          @Param("phone") String phone,
                          @Param("address") String address,
                          @Param("isActive") Boolean isActive,
                          Pageable pageable);
    
    @Query("SELECT c FROM Customer c WHERE c.createdTime BETWEEN :startTime AND :endTime")
    List<Customer> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                            @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    boolean existsByPhone(String phone);
}