package com.farm.finance.service;

import com.farm.finance.entity.InventoryTransaction;
import com.farm.finance.repository.InventoryTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryTransactionService {
    
    private final InventoryTransactionRepository inventoryTransactionRepository;
    
    // ========== 基本查询 ==========
    
    public List<InventoryTransaction> findAll() {
        return inventoryTransactionRepository.findAll();
    }
    
    public Page<InventoryTransaction> findAll(Pageable pageable) {
        return inventoryTransactionRepository.findAll(pageable);
    }
    
    public Optional<InventoryTransaction> findById(Long id) {
        return inventoryTransactionRepository.findById(id);
    }
    
    public Optional<InventoryTransaction> findByTransactionNo(String transactionNo) {
        return inventoryTransactionRepository.findByTransactionNo(transactionNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<InventoryTransaction> findByTransactionType(String transactionType) {
        return inventoryTransactionRepository.findByTransactionType(transactionType);
    }
    
    public List<InventoryTransaction> findByProductId(Long productId) {
        return inventoryTransactionRepository.findByProductId(productId);
    }
    
    public List<InventoryTransaction> findByBatchNo(String batchNo) {
        return inventoryTransactionRepository.findByBatchNo(batchNo);
    }
    
    public List<InventoryTransaction> findByOperatorId(Long operatorId) {
        return inventoryTransactionRepository.findByOperatorId(operatorId);
    }
    
    public List<InventoryTransaction> findByRelatedMoneyId(Long relatedMoneyId) {
        return inventoryTransactionRepository.findByRelatedMoneyId(relatedMoneyId);
    }
    
    public List<InventoryTransaction> findByIsActive(Boolean isActive) {
        return inventoryTransactionRepository.findByIsActive(isActive);
    }
    
    public List<InventoryTransaction> findByIsActiveTrue() {
        return inventoryTransactionRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<InventoryTransaction> findByTransactionDateBetween(LocalDate startDate, LocalDate endDate) {
        return inventoryTransactionRepository.findByTransactionDateBetween(startDate, endDate);
    }
    
    // ========== 自定义查询 ==========
    
    public List<InventoryTransaction> findByProductIdOrderByDateDesc(Long productId) {
        return inventoryTransactionRepository.findByProductIdOrderByDateDesc(productId);
    }
    
    // ========== 分页查询 ==========
    
    public Page<InventoryTransaction> findByProductId(Long productId, Pageable pageable) {
        return inventoryTransactionRepository.findByProductId(productId, pageable);
    }
    
    public Page<InventoryTransaction> findByTransactionType(String transactionType, Pageable pageable) {
        return inventoryTransactionRepository.findByTransactionType(transactionType, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<InventoryTransaction> search(String transactionType, Long productId, Long operatorId, 
                                               Boolean isActive, Pageable pageable) {
        return inventoryTransactionRepository.search(transactionType, productId, operatorId, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<InventoryTransaction> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return inventoryTransactionRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return inventoryTransactionRepository.countByIsActiveTrue();
    }
    
    public BigDecimal sumQuantityByTransactionType(String transactionType) {
        return inventoryTransactionRepository.sumQuantityByTransactionType(transactionType);
    }
    
    public BigDecimal sumAmountByTransactionType(String transactionType) {
        return inventoryTransactionRepository.sumAmountByTransactionType(transactionType);
    }
    
    // ========== 保存和删除 ==========
    
    public InventoryTransaction save(InventoryTransaction transaction) {
        if (transaction.getCreatedTime() == null) {
            transaction.setCreatedTime(LocalDateTime.now());
        }
        return inventoryTransactionRepository.save(transaction);
    }
    
    public void deleteById(Long id) {
        inventoryTransactionRepository.findById(id).ifPresent(transaction -> {
            transaction.setIsActive(false);
            inventoryTransactionRepository.save(transaction);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByTransactionNo(String transactionNo) {
        return inventoryTransactionRepository.existsByTransactionNo(transactionNo);
    }
}