package com.farm.finance.repository;

import com.farm.finance.entity.SysUser;
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
public interface SysUserRepository extends JpaRepository<SysUser, Long> {
    
    // ========== 基本查询 ==========
    
    Optional<SysUser> findByUsername(String username);
    
    Optional<SysUser> findByUsernameAndIsActiveTrue(String username);
    
    boolean existsByUsername(String username);
    
    // ========== 按字段查询 ==========
    
    List<SysUser> findByRealName(String realName);
    
    List<SysUser> findByRealNameContaining(String realName);
    
    Optional<SysUser> findByPhone(String phone);
    
    List<SysUser> findByPhoneContaining(String phone);
    
    List<SysUser> findByRole(String role);
    
    List<SysUser> findByRoleAndIsActiveTrue(String role);
    
    List<SysUser> findByIsActive(Boolean isActive);
    
    List<SysUser> findByIsActiveTrue();
    
    // ========== 分页查询 ==========
    
    Page<SysUser> findByUsernameContaining(String username, Pageable pageable);
    
    Page<SysUser> findByRealNameContaining(String realName, Pageable pageable);
    
    Page<SysUser> findByRole(String role, Pageable pageable);
    
    // ========== 自定义查询 ==========
    
    @Query("SELECT u FROM SysUser u WHERE u.isActive = true ORDER BY u.username")
    List<SysUser> findAllActive();
    
    @Query("SELECT u FROM SysUser u WHERE " +
           "(:username IS NULL OR u.username LIKE %:username%) AND " +
           "(:realName IS NULL OR u.realName LIKE %:realName%) AND " +
           "(:phone IS NULL OR u.phone LIKE %:phone%) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:isActive IS NULL OR u.isActive = :isActive)")
    Page<SysUser> search(@Param("username") String username,
                         @Param("realName") String realName,
                         @Param("phone") String phone,
                         @Param("role") String role,
                         @Param("isActive") Boolean isActive,
                         Pageable pageable);
    
    @Query("SELECT u FROM SysUser u WHERE u.createdTime BETWEEN :startTime AND :endTime")
    List<SysUser> findByCreatedTimeBetween(@Param("startTime") LocalDateTime startTime,
                                           @Param("endTime") LocalDateTime endTime);
    
    // ========== 统计查询 ==========
    
    long countByIsActiveTrue();
    
    long countByRole(String role);
    
    boolean existsByPhone(String phone);
}
