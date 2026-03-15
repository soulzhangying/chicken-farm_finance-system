package com.farm.finance.service;

import com.farm.finance.entity.Inventory;
import com.farm.finance.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {
    
    private final InventoryRepository inventoryRepository;
    
    // ========== 基本查询 ==========
    
    public List<Inventory> findAll() {
        return inventoryRepository.findAll();
    }
    
    public Page<Inventory> findAll(Pageable pageable) {
        return inventoryRepository.findAll(pageable);
    }
    
    public Optional<Inventory> findById(Long id) {
        return inventoryRepository.findById(id);
    }
    
    // ========== 按字段查询 ==========
    
    public List<Inventory> findByProductId(Long productId) {
        return inventoryRepository.findByProductId(productId);
    }
    
    public List<Inventory> findByBatchNo(String batchNo) {
        return inventoryRepository.findByBatchNo(batchNo);
    }
    
    public List<Inventory> findByUnit(String unit) {
        return inventoryRepository.findByUnit(unit);
    }
    
    public List<Inventory> findByLocation(String location) {
        return inventoryRepository.findByLocation(location);
    }
    
    public List<Inventory> findByStatus(Boolean status) {
        return inventoryRepository.findByStatus(status);
    }
    
    public List<Inventory> findByIsActive(Boolean isActive) {
        return inventoryRepository.findByIsActive(isActive);
    }
    
    public List<Inventory> findByIsActiveTrue() {
        return inventoryRepository.findByIsActiveTrue();
    }
    
    // ========== 自定义查询 ==========
    
    public List<Inventory> findActiveByProductId(Long productId) {
        return inventoryRepository.findActiveByProductId(productId);
    }
    
    public BigDecimal sumQuantityByProductId(Long productId) {
        return inventoryRepository.sumQuantityByProductId(productId);
    }
    
    // ========== 分页查询 ==========
    
    public Page<Inventory> findByProductId(Long productId, Pageable pageable) {
        return inventoryRepository.findByProductId(productId, pageable);
    }
    
    public Page<Inventory> findByStatus(Boolean status, Pageable pageable) {
        return inventoryRepository.findByStatus(status, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<Inventory> search(Long productId, Boolean status, Boolean isActive, Pageable pageable) {
        return inventoryRepository.search(productId, status, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<Inventory> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return inventoryRepository.countByIsActiveTrue();
    }
    
    public long countByStatusTrue() {
        return inventoryRepository.countByStatusTrue();
    }
    
    public BigDecimal sumTotalQuantity() {
        return inventoryRepository.sumTotalQuantity();
    }
    
    // ========== 保存和删除 ==========
    
    public Inventory save(Inventory inventory) {
        LocalDateTime now = LocalDateTime.now();
        if (inventory.getCreatedTime() == null) {
            inventory.setCreatedTime(now);
        }
        inventory.setUpdatedTime(now);
        return inventoryRepository.save(inventory);
    }
    
    public void deleteById(Long id) {
        inventoryRepository.findById(id).ifPresent(inventory -> {
            inventory.setIsActive(false);
            inventoryRepository.save(inventory);
        });
    }
}