package com.farm.finance.service;

import com.farm.finance.entity.Supplier;
import com.farm.finance.repository.SupplierRepository;
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
public class SupplierService {
    
    private final SupplierRepository supplierRepository;
    
    // ========== 基本查询 ==========
    
    public List<Supplier> findAll() {
        return supplierRepository.findAll();
    }
    
    public Page<Supplier> findAll(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }
    
    public Optional<Supplier> findById(Long id) {
        return supplierRepository.findById(id);
    }
    
    public Optional<Supplier> findBySupplierNo(String supplierNo) {
        return supplierRepository.findBySupplierNo(supplierNo);
    }
    
    public Optional<Supplier> findByName(String name) {
        return supplierRepository.findByName(name);
    }
    
    // ========== 按字段查询 ==========
    
    public List<Supplier> findByContactPerson(String contactPerson) {
        return supplierRepository.findByContactPerson(contactPerson);
    }
    
    public List<Supplier> findByContactPersonContaining(String contactPerson) {
        return supplierRepository.findByContactPersonContaining(contactPerson);
    }
    
    public Optional<Supplier> findByPhone(String phone) {
        return supplierRepository.findByPhone(phone);
    }
    
    public List<Supplier> findByPhoneContaining(String phone) {
        return supplierRepository.findByPhoneContaining(phone);
    }
    
    public List<Supplier> findByNameContaining(String name) {
        return supplierRepository.findByNameContaining(name);
    }
    
    public List<Supplier> findByIsActive(Boolean isActive) {
        return supplierRepository.findByIsActive(isActive);
    }
    
    public List<Supplier> findByIsActiveTrue() {
        return supplierRepository.findByIsActiveTrue();
    }
    
    public List<Supplier> findAllActive() {
        return supplierRepository.findAllActive();
    }
    
    // ========== 分页查询 ==========
    
    public Page<Supplier> findByNameContaining(String name, Pageable pageable) {
        return supplierRepository.findByNameContaining(name, pageable);
    }
    
    public Page<Supplier> findByPhoneContaining(String phone, Pageable pageable) {
        return supplierRepository.findByPhoneContaining(phone, pageable);
    }
    
    public Page<Supplier> findByContactPersonContaining(String contactPerson, Pageable pageable) {
        return supplierRepository.findByContactPersonContaining(contactPerson, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public List<Supplier> searchByName(String keyword) {
        return supplierRepository.searchByName(keyword);
    }
    
    public Page<Supplier> search(String name, String contactPerson, String phone, 
                                  Boolean isActive, Pageable pageable) {
        return supplierRepository.search(name, contactPerson, phone, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<Supplier> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return supplierRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return supplierRepository.countByIsActiveTrue();
    }
    
    // ========== 保存和删除 ==========
    
    public Supplier save(Supplier supplier) {
        if (supplier.getCreatedTime() == null) {
            supplier.setCreatedTime(LocalDateTime.now());
        }
        return supplierRepository.save(supplier);
    }
    
    public void deleteById(Long id) {
        supplierRepository.findById(id).ifPresent(supplier -> {
            supplier.setIsActive(false);
            supplierRepository.save(supplier);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsBySupplierNo(String supplierNo) {
        return supplierRepository.existsBySupplierNo(supplierNo);
    }
    
    public boolean existsByPhone(String phone) {
        return supplierRepository.existsByPhone(phone);
    }
}