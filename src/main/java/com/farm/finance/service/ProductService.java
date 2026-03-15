package com.farm.finance.service;

import com.farm.finance.entity.Product;
import com.farm.finance.repository.ProductRepository;
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
public class ProductService {
    
    private final ProductRepository productRepository;
    
    // ========== 基本查询 ==========
    
    public List<Product> findAll() {
        return productRepository.findAll();
    }
    
    public Page<Product> findAll(Pageable pageable) {
        return productRepository.findAll(pageable);
    }
    
    public Optional<Product> findById(Long id) {
        return productRepository.findById(id);
    }
    
    public Optional<Product> findByProductNo(String productNo) {
        return productRepository.findByProductNo(productNo);
    }
    
    public Optional<Product> findByName(String name) {
        return productRepository.findByName(name);
    }
    
    // ========== 按字段查询 ==========
    
    public List<Product> findByProductType(String productType) {
        return productRepository.findByProductType(productType);
    }
    
    public List<Product> findByProductTypeAndIsActiveTrue(String productType) {
        return productRepository.findByProductTypeAndIsActiveTrue(productType);
    }
    
    public List<Product> findByUnit(String unit) {
        return productRepository.findByUnit(unit);
    }
    
    public List<Product> findByIsActive(Boolean isActive) {
        return productRepository.findByIsActive(isActive);
    }
    
    public List<Product> findByIsActiveTrue() {
        return productRepository.findByIsActiveTrue();
    }
    
    public List<Product> findAllActive() {
        return productRepository.findAllActive();
    }
    
    public List<Product> findActiveByType(String type) {
        return productRepository.findActiveByType(type);
    }
    
    // ========== 价格范围查询 ==========
    
    public List<Product> findBySalePriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findBySalePriceBetween(minPrice, maxPrice);
    }
    
    public List<Product> findByPurchasePriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return productRepository.findByPurchasePriceBetween(minPrice, maxPrice);
    }
    
    // ========== 分页查询 ==========
    
    public Page<Product> findByProductType(String productType, Pageable pageable) {
        return productRepository.findByProductType(productType, pageable);
    }
    
    public Page<Product> findByUnit(String unit, Pageable pageable) {
        return productRepository.findByUnit(unit, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<Product> search(String productType, Boolean isActive, Pageable pageable) {
        return productRepository.search(productType, isActive, pageable);
    }
    
    public Page<Product> searchWithPrice(String productType, BigDecimal minPrice, 
                                          BigDecimal maxPrice, Boolean isActive, Pageable pageable) {
        return productRepository.searchWithPrice(productType, minPrice, maxPrice, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<Product> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return productRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return productRepository.countByIsActiveTrue();
    }
    
    public long countByProductType(String productType) {
        return productRepository.countByProductType(productType);
    }
    
    public long countByProductTypeAndIsActiveTrue(String productType) {
        return productRepository.countByProductTypeAndIsActiveTrue(productType);
    }
    
    public List<Object[]> countGroupByProductType() {
        return productRepository.countGroupByProductType();
    }
    
    // ========== 保存和删除 ==========
    
    public Product save(Product product) {
        if (product.getCreatedTime() == null) {
            product.setCreatedTime(LocalDateTime.now());
        }
        return productRepository.save(product);
    }
    
    public void deleteById(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setIsActive(false);
            productRepository.save(product);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByProductNo(String productNo) {
        return productRepository.existsByProductNo(productNo);
    }
    
    public boolean existsByName(String name) {
        return productRepository.existsByName(name);
    }
}
