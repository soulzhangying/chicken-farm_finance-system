package com.farm.finance.service;

import com.farm.finance.entity.SalesOrderItem;
import com.farm.finance.repository.SalesOrderItemRepository;
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
public class SalesOrderItemService {
    
    private final SalesOrderItemRepository salesOrderItemRepository;
    
    // ========== 基本查询 ==========
    
    public List<SalesOrderItem> findAll() {
        return salesOrderItemRepository.findAll();
    }
    
    public Page<SalesOrderItem> findAll(Pageable pageable) {
        return salesOrderItemRepository.findAll(pageable);
    }
    
    public Optional<SalesOrderItem> findById(Long id) {
        return salesOrderItemRepository.findById(id);
    }
    
    // ========== 按字段查询 ==========
    
    public List<SalesOrderItem> findByOrderId(Long orderId) {
        return salesOrderItemRepository.findByOrderId(orderId);
    }
    
    public List<SalesOrderItem> findByProductId(Long productId) {
        return salesOrderItemRepository.findByProductId(productId);
    }
    
    public List<SalesOrderItem> findByBatchNo(String batchNo) {
        return salesOrderItemRepository.findByBatchNo(batchNo);
    }
    
    public List<SalesOrderItem> findByUnit(String unit) {
        return salesOrderItemRepository.findByUnit(unit);
    }
    
    public List<SalesOrderItem> findByIsActive(Boolean isActive) {
        return salesOrderItemRepository.findByIsActive(isActive);
    }
    
    public List<SalesOrderItem> findByIsActiveTrue() {
        return salesOrderItemRepository.findByIsActiveTrue();
    }
    
    // ========== 分页查询 ==========
    
    public Page<SalesOrderItem> findByOrderId(Long orderId, Pageable pageable) {
        return salesOrderItemRepository.findByOrderId(orderId, pageable);
    }
    
    public Page<SalesOrderItem> findByProductId(Long productId, Pageable pageable) {
        return salesOrderItemRepository.findByProductId(productId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<SalesOrderItem> search(Long orderId, Long productId, Boolean isActive, Pageable pageable) {
        return salesOrderItemRepository.search(orderId, productId, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<SalesOrderItem> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return salesOrderItemRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return salesOrderItemRepository.countByIsActiveTrue();
    }
    
    public long countByOrderId(Long orderId) {
        return salesOrderItemRepository.countByOrderId(orderId);
    }
    
    public BigDecimal sumTotalPriceByOrderId(Long orderId) {
        return salesOrderItemRepository.sumTotalPriceByOrderId(orderId);
    }
    
    public BigDecimal sumQuantityByOrderId(Long orderId) {
        return salesOrderItemRepository.sumQuantityByOrderId(orderId);
    }
    
    // ========== 保存和删除 ==========
    
    public SalesOrderItem save(SalesOrderItem item) {
        if (item.getCreatedTime() == null) {
            item.setCreatedTime(LocalDateTime.now());
        }
        return salesOrderItemRepository.save(item);
    }
    
    public void deleteById(Long id) {
        salesOrderItemRepository.findById(id).ifPresent(item -> {
            item.setIsActive(false);
            salesOrderItemRepository.save(item);
        });
    }
}