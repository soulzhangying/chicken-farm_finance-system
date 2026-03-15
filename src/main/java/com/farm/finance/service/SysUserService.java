package com.farm.finance.service;

import com.farm.finance.entity.SysUser;
import com.farm.finance.repository.SysUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SysUserService {
    
    private final SysUserRepository sysUserRepository;
    
    // ========== 基本查询 ==========
    
    public List<SysUser> findAll() {
        return sysUserRepository.findAll();
    }
    
    public Page<SysUser> findAll(Pageable pageable) {
        return sysUserRepository.findAll(pageable);
    }
    
    public Optional<SysUser> findById(Long id) {
        return sysUserRepository.findById(id);
    }
    
    public Optional<SysUser> findByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
    
    public Optional<SysUser> findByUsernameAndIsActiveTrue(String username) {
        return sysUserRepository.findByUsernameAndIsActiveTrue(username);
    }
    
    // ========== 按字段查询 ==========
    
    public List<SysUser> findByRealName(String realName) {
        return sysUserRepository.findByRealName(realName);
    }
    
    public List<SysUser> findByRealNameContaining(String realName) {
        return sysUserRepository.findByRealNameContaining(realName);
    }
    
    public Optional<SysUser> findByPhone(String phone) {
        return sysUserRepository.findByPhone(phone);
    }
    
    public List<SysUser> findByPhoneContaining(String phone) {
        return sysUserRepository.findByPhoneContaining(phone);
    }
    
    public List<SysUser> findByRole(String role) {
        return sysUserRepository.findByRole(role);
    }
    
    public List<SysUser> findByRoleAndIsActiveTrue(String role) {
        return sysUserRepository.findByRoleAndIsActiveTrue(role);
    }
    
    public List<SysUser> findByIsActive(Boolean isActive) {
        return sysUserRepository.findByIsActive(isActive);
    }
    
    public List<SysUser> findAllActive() {
        return sysUserRepository.findAllActive();
    }
    
    // ========== 分页查询 ==========
    
    public Page<SysUser> findByUsernameContaining(String username, Pageable pageable) {
        return sysUserRepository.findByUsernameContaining(username, pageable);
    }
    
    public Page<SysUser> findByRealNameContaining(String realName, Pageable pageable) {
        return sysUserRepository.findByRealNameContaining(realName, pageable);
    }
    
    public Page<SysUser> findByRole(String role, Pageable pageable) {
        return sysUserRepository.findByRole(role, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<SysUser> search(String username, String realName, String phone, 
                                 String role, Boolean isActive, Pageable pageable) {
        return sysUserRepository.search(username, realName, phone, role, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<SysUser> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return sysUserRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return sysUserRepository.countByIsActiveTrue();
    }
    
    public long countByRole(String role) {
        return sysUserRepository.countByRole(role);
    }
    
    // ========== 保存和删除 ==========
    
    public SysUser save(SysUser user) {
        if (user.getCreatedTime() == null) {
            user.setCreatedTime(LocalDateTime.now());
        }
        return sysUserRepository.save(user);
    }
    
    public void deleteById(Long id) {
        sysUserRepository.findById(id).ifPresent(user -> {
            user.setIsActive(false);
            sysUserRepository.save(user);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByUsername(String username) {
        return sysUserRepository.existsByUsername(username);
    }
    
    public boolean existsByPhone(String phone) {
        return sysUserRepository.existsByPhone(phone);
    }
}