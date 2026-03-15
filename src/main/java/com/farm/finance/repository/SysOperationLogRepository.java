package com.farm.finance.repository;

import com.farm.finance.entity.SysOperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SysOperationLogRepository extends JpaRepository<SysOperationLog, Long> {
    
    // ========== 按字段查询 ==========
    
    List<SysOperationLog> findByUserId(Long userId);
    
    List<SysOperationLog> findByUsername(String username);
    
    List<SysOperationLog> findByOperation(String operation);
    
    List<SysOperationLog> findByIp(String ip);
    
    List<SysOperationLog> findByStatus(Boolean status);
    
    List<SysOperationLog> findByIsActive(Boolean isActive);
    
    List<SysOperationLog> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<SysOperationLog> findByUserId(Long userId, Pageable pageable);
    
    Page<SysOperationLog> findByOperation(String operation, Pageable pageable);
    
    Page<SysOperationLog> findByStatus(Boolean status, Pageable pageable);
    
    // ========== 组合搜索 ==========
    
    @Query("SELECT l FROM SysOperationLog l WHERE " +
           "(:userId IS NULL OR l.userId = :userId) AND " +
           "(:operation IS NULL OR l.operation = :operation) AND " +
           "(:status IS NULL OR l.status = :status) AND " +
           "(:isActive IS NULL OR l.isActive = :isActive)")
    Page<SysOperationLog> search(@Param("userId") Long userId,
                                  @Param("operation") String operation,
                                  @Param("status") Boolean status,
                                  @Param("isActive") Boolean isActive,
                                  Pageable pageable);
    
    @Query("SELECT l FROM SysOperationLog l WHERE l.createdTime BETWEEN :startTime AND :endTime")
    List<SysOperationLog> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                                     @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByUserId(Long userId);
    
    long countByOperation(String operation);
    
    long countByStatus(Boolean status);
}