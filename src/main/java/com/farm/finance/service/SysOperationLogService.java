package com.farm.finance.service;

import com.farm.finance.entity.SysOperationLog;
import com.farm.finance.repository.SysOperationLogRepository;
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
public class SysOperationLogService {
    
    private final SysOperationLogRepository sysOperationLogRepository;
    
    // ========== 基本查询 ==========
    
    public List<SysOperationLog> findAll() {
        return sysOperationLogRepository.findAll();
    }
    
    public Page<SysOperationLog> findAll(Pageable pageable) {
        return sysOperationLogRepository.findAll(pageable);
    }
    
    public Optional<SysOperationLog> findById(Long id) {
        return sysOperationLogRepository.findById(id);
    }
    
    // ========== 按字段查询 ==========
    
    public List<SysOperationLog> findByUserId(Long userId) {
        return sysOperationLogRepository.findByUserId(userId);
    }
    
    public List<SysOperationLog> findByUsername(String username) {
        return sysOperationLogRepository.findByUsername(username);
    }
    
    public List<SysOperationLog> findByOperation(String operation) {
        return sysOperationLogRepository.findByOperation(operation);
    }
    
    public List<SysOperationLog> findByIp(String ip) {
        return sysOperationLogRepository.findByIp(ip);
    }
    
    public List<SysOperationLog> findByStatus(Boolean status) {
        return sysOperationLogRepository.findByStatus(status);
    }
    
    public List<SysOperationLog> findByIsActive(Boolean isActive) {
        return sysOperationLogRepository.findByIsActive(isActive);
    }
    
    public List<SysOperationLog> findByIsActiveTrue() {
        return sysOperationLogRepository.findByIsActiveTrue();
    }
    
    // ========== 分页查询 ==========
    
    public Page<SysOperationLog> findByUserId(Long userId, Pageable pageable) {
        return sysOperationLogRepository.findByUserId(userId, pageable);
    }
    
    public Page<SysOperationLog> findByOperation(String operation, Pageable pageable) {
        return sysOperationLogRepository.findByOperation(operation, pageable);
    }
    
    public Page<SysOperationLog> findByStatus(Boolean status, Pageable pageable) {
        return sysOperationLogRepository.findByStatus(status, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<SysOperationLog> search(Long userId, String operation, Boolean status, 
                                          Boolean isActive, Pageable pageable) {
        return sysOperationLogRepository.search(userId, operation, status, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<SysOperationLog> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return sysOperationLogRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return sysOperationLogRepository.countByIsActiveTrue();
    }
    
    public long countByUserId(Long userId) {
        return sysOperationLogRepository.countByUserId(userId);
    }
    
    public long countByOperation(String operation) {
        return sysOperationLogRepository.countByOperation(operation);
    }
    
    public long countByStatus(Boolean status) {
        return sysOperationLogRepository.countByStatus(status);
    }
    
    // ========== 保存和删除 ==========
    
    public SysOperationLog save(SysOperationLog log) {
        if (log.getCreatedTime() == null) {
            log.setCreatedTime(LocalDateTime.now());
        }
        return sysOperationLogRepository.save(log);
    }
    
    public void deleteById(Long id) {
        sysOperationLogRepository.findById(id).ifPresent(log -> {
            log.setIsActive(false);
            sysOperationLogRepository.save(log);
        });
    }
}