package com.farm.finance.service;

import com.farm.finance.entity.ChickenSale;
import com.farm.finance.repository.ChickenSaleRepository;
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
public class ChickenSaleService {
    
    private final ChickenSaleRepository chickenSaleRepository;
    
    // ========== 基本查询 ==========
    
    public List<ChickenSale> findAll() {
        return chickenSaleRepository.findAll();
    }
    
    public Page<ChickenSale> findAll(Pageable pageable) {
        return chickenSaleRepository.findAll(pageable);
    }
    
    public Optional<ChickenSale> findById(Long id) {
        return chickenSaleRepository.findById(id);
    }
    
    public Optional<ChickenSale> findBySaleNo(String saleNo) {
        return chickenSaleRepository.findBySaleNo(saleNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<ChickenSale> findByBatchId(Long batchId) {
        return chickenSaleRepository.findByBatchId(batchId);
    }
    
    public List<ChickenSale> findByCustomerId(Long customerId) {
        return chickenSaleRepository.findByCustomerId(customerId);
    }
    
    public List<ChickenSale> findBySaleDate(LocalDate saleDate) {
        return chickenSaleRepository.findBySaleDate(saleDate);
    }
    
    public List<ChickenSale> findByIsActive(Boolean isActive) {
        return chickenSaleRepository.findByIsActive(isActive);
    }
    
    public List<ChickenSale> findByIsActiveTrue() {
        return chickenSaleRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<ChickenSale> findBySaleDateBetween(LocalDate startDate, LocalDate endDate) {
        return chickenSaleRepository.findBySaleDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<ChickenSale> findByBatchId(Long batchId, Pageable pageable) {
        return chickenSaleRepository.findByBatchId(batchId, pageable);
    }
    
    public Page<ChickenSale> findByCustomerId(Long customerId, Pageable pageable) {
        return chickenSaleRepository.findByCustomerId(customerId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<ChickenSale> search(Long batchId, Long customerId, Boolean isActive, Pageable pageable) {
        return chickenSaleRepository.search(batchId, customerId, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<ChickenSale> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return chickenSaleRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return chickenSaleRepository.countByIsActiveTrue();
    }
    
    public long countByBatchId(Long batchId) {
        return chickenSaleRepository.countByBatchId(batchId);
    }
    
    public BigDecimal sumTotalAmount() {
        return chickenSaleRepository.sumTotalAmount();
    }
    
    public Integer sumQuantityByBatchId(Long batchId) {
        return chickenSaleRepository.sumQuantityByBatchId(batchId);
    }
    
    public BigDecimal sumWeightByBatchId(Long batchId) {
        return chickenSaleRepository.sumWeightByBatchId(batchId);
    }
    
    public BigDecimal sumTotalAmountByBatchId(Long batchId) {
        return chickenSaleRepository.sumTotalAmountByBatchId(batchId);
    }
    
    // ========== 保存和删除 ==========
    
    public ChickenSale save(ChickenSale sale) {
        if (sale.getCreatedTime() == null) {
            sale.setCreatedTime(LocalDateTime.now());
        }
        return chickenSaleRepository.save(sale);
    }
    
    public void deleteById(Long id) {
        chickenSaleRepository.findById(id).ifPresent(sale -> {
            sale.setIsActive(false);
            chickenSaleRepository.save(sale);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsBySaleNo(String saleNo) {
        return chickenSaleRepository.existsBySaleNo(saleNo);
    }
    
    // ========== 关键词和日期范围搜索 ==========
    
    public List<ChickenSale> searchByKeyword(String keyword) {
        return chickenSaleRepository.searchByKeyword(keyword);
    }
    
    public List<ChickenSale> searchByBatchIdAndDateRange(Long batchId, LocalDate startDate, LocalDate endDate) {
        return chickenSaleRepository.searchByBatchIdAndDateRange(batchId, startDate, endDate);
    }
    
    // ========== 按批次汇总销售额 ==========
    
    public List<Object[]> sumByBatchId() {
        return chickenSaleRepository.sumByBatchId();
    }
    
    public List<Object[]> sumByBatchIdAndDateRange(LocalDate startDate, LocalDate endDate) {
        return chickenSaleRepository.sumByBatchIdAndDateRange(startDate, endDate);
    }
}
