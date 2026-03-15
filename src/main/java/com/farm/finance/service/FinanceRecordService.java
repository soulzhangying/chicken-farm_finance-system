package com.farm.finance.service;

import com.farm.finance.entity.FinanceRecord;
import com.farm.finance.repository.FinanceRecordRepository;
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
public class FinanceRecordService {
    
    private final FinanceRecordRepository financeRecordRepository;
    
    // ========== 基本查询 ==========
    
    public List<FinanceRecord> findAll() {
        return financeRecordRepository.findAll();
    }
    
    public Page<FinanceRecord> findAll(Pageable pageable) {
        return financeRecordRepository.findAll(pageable);
    }
    
    public Optional<FinanceRecord> findById(Long id) {
        return financeRecordRepository.findById(id);
    }
    
    public Optional<FinanceRecord> findByRecordNo(String recordNo) {
        return financeRecordRepository.findByRecordNo(recordNo);
    }
    
    // ========== 按字段查询 ==========
    
    public List<FinanceRecord> findByMoneyType(String moneyType) {
        return financeRecordRepository.findByMoneyType(moneyType);
    }
    
    public List<FinanceRecord> findByCostType(String costType) {
        return financeRecordRepository.findByCostType(costType);
    }
    
    public List<FinanceRecord> findByIncomeType(String incomeType) {
        return financeRecordRepository.findByIncomeType(incomeType);
    }
    
    public List<FinanceRecord> findByProductId(Long productId) {
        return financeRecordRepository.findByProductId(productId);
    }
    
    public List<FinanceRecord> findByBatchNo(String batchNo) {
        return financeRecordRepository.findByBatchNo(batchNo);
    }
    
    public List<FinanceRecord> findByCustomerId(Long customerId) {
        return financeRecordRepository.findByCustomerId(customerId);
    }
    
    public List<FinanceRecord> findBySupplierId(Long supplierId) {
        return financeRecordRepository.findBySupplierId(supplierId);
    }
    
    public List<FinanceRecord> findByCreatedBy(Long createdBy) {
        return financeRecordRepository.findByCreatedBy(createdBy);
    }
    
    public List<FinanceRecord> findByPaymentMethod(String paymentMethod) {
        return financeRecordRepository.findByPaymentMethod(paymentMethod);
    }
    
    public List<FinanceRecord> findByIsActive(Boolean isActive) {
        return financeRecordRepository.findByIsActive(isActive);
    }
    
    public List<FinanceRecord> findByIsActiveTrue() {
        return financeRecordRepository.findByIsActiveTrue();
    }
    
    // ========== 日期范围查询 ==========
    
    public List<FinanceRecord> findByRecordDateBetween(LocalDate startDate, LocalDate endDate) {
        return financeRecordRepository.findByRecordDateBetween(startDate, endDate);
    }
    
    // ========== 分页查询 ==========
    
    public Page<FinanceRecord> findByMoneyType(String moneyType, Pageable pageable) {
        return financeRecordRepository.findByMoneyType(moneyType, pageable);
    }
    
    public Page<FinanceRecord> findByCustomerId(Long customerId, Pageable pageable) {
        return financeRecordRepository.findByCustomerId(customerId, pageable);
    }
    
    public Page<FinanceRecord> findBySupplierId(Long supplierId, Pageable pageable) {
        return financeRecordRepository.findBySupplierId(supplierId, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<FinanceRecord> search(String moneyType, String costType, String incomeType, 
                                        Long customerId, Long supplierId, Boolean isActive, 
                                        Pageable pageable) {
        return financeRecordRepository.search(moneyType, costType, incomeType, customerId, supplierId, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<FinanceRecord> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return financeRecordRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return financeRecordRepository.countByIsActiveTrue();
    }
    
    public BigDecimal sumIncomeByDateRange(LocalDate startDate, LocalDate endDate) {
        return financeRecordRepository.sumIncomeByDateRange(startDate, endDate);
    }
    
    public BigDecimal sumCostByDateRange(LocalDate startDate, LocalDate endDate) {
        return financeRecordRepository.sumCostByDateRange(startDate, endDate);
    }
    
    public BigDecimal sumTotalIncome() {
        return financeRecordRepository.sumTotalIncome();
    }
    
    public BigDecimal sumTotalCost() {
        return financeRecordRepository.sumTotalCost();
    }
    
    // ========== 保存和删除 ==========
    
    public FinanceRecord save(FinanceRecord record) {
        if (record.getCreatedTime() == null) {
            record.setCreatedTime(LocalDateTime.now());
        }
        return financeRecordRepository.save(record);
    }
    
    public void deleteById(Long id) {
        financeRecordRepository.findById(id).ifPresent(record -> {
            record.setIsActive(false);
            financeRecordRepository.save(record);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByRecordNo(String recordNo) {
        return financeRecordRepository.existsByRecordNo(recordNo);
    }
}