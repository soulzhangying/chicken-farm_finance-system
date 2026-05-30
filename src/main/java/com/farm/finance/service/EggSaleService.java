package com.farm.finance.service;

import com.farm.finance.entity.EggSale;
import com.farm.finance.repository.EggSaleRepository;
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
public class EggSaleService {
    
    private final EggSaleRepository eggSaleRepository;
    
    // ========== 基本查询 ==========
    
    public List<EggSale> findAll() {
        return eggSaleRepository.findAll();
    }
    
    public Page<EggSale> findAll(Pageable pageable) {
        return eggSaleRepository.findAll(pageable);
    }
    
    public Optional<EggSale> findById(Long id) {
        return eggSaleRepository.findById(id);
    }
    
    public Optional<EggSale> findBySaleNo(String saleNo) {
        return eggSaleRepository.findBySaleNo(saleNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<EggSale> findByProductId(Long productId) {
        return eggSaleRepository.findByProductId(productId);
    }
    
    public List<EggSale> findByCustomerId(Long customerId) {
        return eggSaleRepository.findByCustomerId(customerId);
    }
    
    public List<EggSale> findByCreatedBy(Long createdBy) {
        return eggSaleRepository.findByCreatedBy(createdBy);
    }
    
    public List<EggSale> findByPaymentMethod(String paymentMethod) {
        return eggSaleRepository.findByPaymentMethod(paymentMethod);
    }
    
    public List<EggSale> findByIsActive(Boolean isActive) {
        return eggSaleRepository.findByIsActive(isActive);
    }
    
    public List<EggSale> findByIsActiveTrue() {
        return eggSaleRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<EggSale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate) {
        return eggSaleRepository.findBySaleDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<EggSale> findByProductId(Long productId, Pageable pageable) {
        return eggSaleRepository.findByProductId(productId, pageable);
    }
    
    public Page<EggSale> findByCustomerId(Long customerId, Pageable pageable) {
        return eggSaleRepository.findByCustomerId(customerId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<EggSale> search(Long productId, Long customerId, String paymentMethod, 
                                  Boolean isActive, Pageable pageable) {
        return eggSaleRepository.search(productId, customerId, paymentMethod, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<EggSale> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return eggSaleRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return eggSaleRepository.countByIsActiveTrue();
    }
    
    public BigDecimal sumAmountByDateRange(LocalDate startDate, LocalDate endDate) {
        return eggSaleRepository.sumAmountByDateRange(startDate, endDate);
    }
    
    public Integer sumQuantityByDateRange(LocalDate startDate, LocalDate endDate) {
        return eggSaleRepository.sumQuantityByDateRange(startDate, endDate);
    }
    
    public BigDecimal sumTotalAmount() {
        return eggSaleRepository.sumTotalAmount();
    }
    
    public Integer sumTotalQuantity() {
        return eggSaleRepository.sumTotalQuantity();
    }
    
    // ========== 保存和删除 ==========
    
    public EggSale save(EggSale sale) {
        if (sale.getCreatedTime() == null) {
            sale.setCreatedTime(LocalDateTime.now());
        }
        return eggSaleRepository.save(sale);
    }
    
    public void deleteById(Long id) {
        eggSaleRepository.findById(id).ifPresent(sale -> {
            sale.setIsActive(false);
            eggSaleRepository.save(sale);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsBySaleNo(String saleNo) {
        return eggSaleRepository.existsBySaleNo(saleNo);
    }
    
    // ========== 关键词和日期范围搜索 ==========
    
    public List<EggSale> searchByKeyword(String keyword) {
        return eggSaleRepository.searchByKeyword(keyword);
    }
    
    public List<EggSale> searchByDateRange(LocalDate startDate, LocalDate endDate) {
        return eggSaleRepository.searchByDateRange(startDate, endDate);
    }
}