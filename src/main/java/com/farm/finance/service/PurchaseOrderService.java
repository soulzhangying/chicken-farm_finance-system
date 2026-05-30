package com.farm.finance.service;

import com.farm.finance.entity.PurchaseOrder;
import com.farm.finance.repository.PurchaseOrderRepository;
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
public class PurchaseOrderService {
    
    private final PurchaseOrderRepository purchaseOrderRepository;
    
    // ========== 基本查询 ==========
    
    public List<PurchaseOrder> findAll() {
        return purchaseOrderRepository.findAll();
    }
    
    public Page<PurchaseOrder> findAll(Pageable pageable) {
        return purchaseOrderRepository.findAll(pageable);
    }
    
    public Optional<PurchaseOrder> findById(Long id) {
        return purchaseOrderRepository.findById(id);
    }
    
    public Optional<PurchaseOrder> findByOrderNo(String orderNo) {
        return purchaseOrderRepository.findByOrderNo(orderNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<PurchaseOrder> findBySupplierId(Long supplierId) {
        return purchaseOrderRepository.findBySupplierId(supplierId);
    }
    
    public List<PurchaseOrder> findByCreatedBy(Long createdBy) {
        return purchaseOrderRepository.findByCreatedBy(createdBy);
    }
    
    public List<PurchaseOrder> findByPaymentStatus(String paymentStatus) {
        return purchaseOrderRepository.findByPaymentStatus(paymentStatus);
    }
    
    public List<PurchaseOrder> findByDeliveryStatus(String deliveryStatus) {
        return purchaseOrderRepository.findByDeliveryStatus(deliveryStatus);
    }
    
    public List<PurchaseOrder> findByIsActive(Boolean isActive) {
        return purchaseOrderRepository.findByIsActive(isActive);
    }
    
    public List<PurchaseOrder> findByIsActiveTrue() {
        return purchaseOrderRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<PurchaseOrder> findByOrderDateBetween(LocalDate startDate, LocalDate endDate) {
        return purchaseOrderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<PurchaseOrder> findBySupplierId(Long supplierId, Pageable pageable) {
        return purchaseOrderRepository.findBySupplierId(supplierId, pageable);
    }
    
    public Page<PurchaseOrder> findByPaymentStatus(String paymentStatus, Pageable pageable) {
        return purchaseOrderRepository.findByPaymentStatus(paymentStatus, pageable);
    }
    
    public Page<PurchaseOrder> findByDeliveryStatus(String deliveryStatus, Pageable pageable) {
        return purchaseOrderRepository.findByDeliveryStatus(deliveryStatus, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<PurchaseOrder> search(Long supplierId, String paymentStatus, String deliveryStatus, 
                                        Boolean isActive, Pageable pageable) {
        return purchaseOrderRepository.search(supplierId, paymentStatus, deliveryStatus, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<PurchaseOrder> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return purchaseOrderRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return purchaseOrderRepository.countByIsActiveTrue();
    }
    
    public long countByPaymentStatus(String paymentStatus) {
        return purchaseOrderRepository.countByPaymentStatus(paymentStatus);
    }
    
    public long countByDeliveryStatus(String deliveryStatus) {
        return purchaseOrderRepository.countByDeliveryStatus(deliveryStatus);
    }
    
    public BigDecimal sumTotalAmount() {
        return purchaseOrderRepository.sumTotalAmount();
    }
    
    public BigDecimal sumAmountByPaymentStatus(String paymentStatus) {
        return purchaseOrderRepository.sumAmountByPaymentStatus(paymentStatus);
    }
    
    // ========== 保存和删除 ==========
    
    public PurchaseOrder save(PurchaseOrder order) {
        if (order.getCreatedTime() == null) {
            order.setCreatedTime(LocalDateTime.now());
        }
        return purchaseOrderRepository.save(order);
    }
    
    public void deleteById(Long id) {
        purchaseOrderRepository.findById(id).ifPresent(order -> {
            order.setIsActive(false);
            purchaseOrderRepository.save(order);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByOrderNo(String orderNo) {
        return purchaseOrderRepository.existsByOrderNo(orderNo);
    }
    
    // ========== 关键词搜索 ==========
    
    public List<PurchaseOrder> searchByKeyword(String keyword) {
        return purchaseOrderRepository.searchByKeyword(keyword);
    }
}