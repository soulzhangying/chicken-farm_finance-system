package com.farm.finance.service;

import com.farm.finance.entity.SalesOrder;
import com.farm.finance.repository.SalesOrderRepository;
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
public class SalesOrderService {
    
    private final SalesOrderRepository salesOrderRepository;
    
    // ========== 基本查询 ==========
    
    public List<SalesOrder> findAll() {
        return salesOrderRepository.findAll();
    }
    
    public Page<SalesOrder> findAll(Pageable pageable) {
        return salesOrderRepository.findAll(pageable);
    }
    
    public Optional<SalesOrder> findById(Long id) {
        return salesOrderRepository.findById(id);
    }
    
    public Optional<SalesOrder> findByOrderNo(String orderNo) {
        return salesOrderRepository.findByOrderNo(orderNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<SalesOrder> findByCustomerId(Long customerId) {
        return salesOrderRepository.findByCustomerId(customerId);
    }
    
    public List<SalesOrder> findByPaymentStatus(String paymentStatus) {
        return salesOrderRepository.findByPaymentStatus(paymentStatus);
    }
    
    public List<SalesOrder> findByOrderStatus(String orderStatus) {
        return salesOrderRepository.findByOrderStatus(orderStatus);
    }
    
    public List<SalesOrder> findByDeliveryType(String deliveryType) {
        return salesOrderRepository.findByDeliveryType(deliveryType);
    }
    
    public List<SalesOrder> findByIsActive(Boolean isActive) {
        return salesOrderRepository.findByIsActive(isActive);
    }
    
    public List<SalesOrder> findByIsActiveTrue() {
        return salesOrderRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<SalesOrder> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return salesOrderRepository.findByOrderDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<SalesOrder> findByCustomerId(Long customerId, Pageable pageable) {
        return salesOrderRepository.findByCustomerId(customerId, pageable);
    }
    
    public Page<SalesOrder> findByPaymentStatus(String paymentStatus, Pageable pageable) {
        return salesOrderRepository.findByPaymentStatus(paymentStatus, pageable);
    }
    
    public Page<SalesOrder> findByOrderStatus(String orderStatus, Pageable pageable) {
        return salesOrderRepository.findByOrderStatus(orderStatus, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<SalesOrder> search(Long customerId, String paymentStatus, String orderStatus, 
                                     Boolean isActive, Pageable pageable) {
        return salesOrderRepository.search(customerId, paymentStatus, orderStatus, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<SalesOrder> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return salesOrderRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return salesOrderRepository.countByIsActiveTrue();
    }
    
    public long countByPaymentStatus(String paymentStatus) {
        return salesOrderRepository.countByPaymentStatus(paymentStatus);
    }
    
    public long countByOrderStatus(String orderStatus) {
        return salesOrderRepository.countByOrderStatus(orderStatus);
    }
    
    public BigDecimal sumTotalAmount() {
        return salesOrderRepository.sumTotalAmount();
    }
    
    public BigDecimal sumActualAmount() {
        return salesOrderRepository.sumActualAmount();
    }
    
    public BigDecimal sumAmountByPaymentStatus(String paymentStatus) {
        return salesOrderRepository.sumAmountByPaymentStatus(paymentStatus);
    }
    
    // ========== 保存和删除 ==========
    
    public SalesOrder save(SalesOrder order) {
        if (order.getCreatedTime() == null) {
            order.setCreatedTime(LocalDateTime.now());
        }
        return salesOrderRepository.save(order);
    }
    
    public void deleteById(Long id) {
        salesOrderRepository.findById(id).ifPresent(order -> {
            order.setIsActive(false);
            salesOrderRepository.save(order);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByOrderNo(String orderNo) {
        return salesOrderRepository.existsByOrderNo(orderNo);
    }
}