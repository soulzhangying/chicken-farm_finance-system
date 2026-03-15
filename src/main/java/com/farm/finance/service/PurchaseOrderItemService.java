package com.farm.finance.service;

import com.farm.finance.entity.PurchaseOrderItem;
import com.farm.finance.repository.PurchaseOrderItemRepository;
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
public class PurchaseOrderItemService {
    
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;
    
    // ========== 基本查询 ==========
    
    public List<PurchaseOrderItem> findAll() {
        return purchaseOrderItemRepository.findAll();
    }
    
    public Page<PurchaseOrderItem> findAll(Pageable pageable) {
        return purchaseOrderItemRepository.findAll(pageable);
    }
    
    public Optional<PurchaseOrderItem> findById(Long id) {
        return purchaseOrderItemRepository.findById(id);
    }
    
    // ========== 按字段查询 ==========
    
    public List<PurchaseOrderItem> findByOrderId(Long orderId) {
        return purchaseOrderItemRepository.findByOrderId(orderId);
    }
    
    public List<PurchaseOrderItem> findByProductId(Long productId) {
        return purchaseOrderItemRepository.findByProductId(productId);
    }
    
    public List<PurchaseOrderItem> findByUnit(String unit) {
        return purchaseOrderItemRepository.findByUnit(unit);
    }
    
    public List<PurchaseOrderItem> findByIsActive(Boolean isActive) {
        return purchaseOrderItemRepository.findByIsActive(isActive);
    }
    
    public List<PurchaseOrderItem> findByIsActiveTrue() {
        return purchaseOrderItemRepository.findByIsActiveTrue();
    }
    
    // ========== 分页查询 ==========
    
    public Page<PurchaseOrderItem> findByOrderId(Long orderId, Pageable pageable) {
        return purchaseOrderItemRepository.findByOrderId(orderId, pageable);
    }
    
    public Page<PurchaseOrderItem> findByProductId(Long productId, Pageable pageable) {
        return purchaseOrderItemRepository.findByProductId(productId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<PurchaseOrderItem> search(Long orderId, Long productId, Boolean isActive, Pageable pageable) {
        return purchaseOrderItemRepository.search(orderId, productId, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<PurchaseOrderItem> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return purchaseOrderItemRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return purchaseOrderItemRepository.countByIsActiveTrue();
    }
    
    public long countByOrderId(Long orderId) {
        return purchaseOrderItemRepository.countByOrderId(orderId);
    }
    
    public BigDecimal sumTotalPriceByOrderId(Long orderId) {
        return purchaseOrderItemRepository.sumTotalPriceByOrderId(orderId);
    }
    
    public BigDecimal sumQuantityByOrderId(Long orderId) {
        return purchaseOrderItemRepository.sumQuantityByOrderId(orderId);
    }
    
    // ========== 保存和删除 ==========
    
    public PurchaseOrderItem save(PurchaseOrderItem item) {
        if (item.getCreatedTime() == null) {
            item.setCreatedTime(LocalDateTime.now());
        }
        return purchaseOrderItemRepository.save(item);
    }
    
    public void deleteById(Long id) {
        purchaseOrderItemRepository.findById(id).ifPresent(item -> {
            item.setIsActive(false);
            purchaseOrderItemRepository.save(item);
        });
    }
}