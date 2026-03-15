package com.farm.finance.service;

import com.farm.finance.entity.ChickenHouse;
import com.farm.finance.repository.ChickenHouseRepository;
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
public class ChickenHouseService {
    
    private final ChickenHouseRepository chickenHouseRepository;
    
    // ========== 基本查询 ==========
    
    public List<ChickenHouse> findAll() {
        return chickenHouseRepository.findAll();
    }
    
    public Page<ChickenHouse> findAll(Pageable pageable) {
        return chickenHouseRepository.findAll(pageable);
    }
    
    public Optional<ChickenHouse> findById(Long id) {
        return chickenHouseRepository.findById(id);
    }
    
    public Optional<ChickenHouse> findByHouseNo(String houseNo) {
        return chickenHouseRepository.findByHouseNo(houseNo);
    }
    
    public Optional<ChickenHouse> findByName(String name) {
        return chickenHouseRepository.findByName(name);
    }
    
    // ========== 按字段查询 ==========
    
    public List<ChickenHouse> findByStatus(String status) {
        return chickenHouseRepository.findByStatus(status);
    }
    
    public List<ChickenHouse> findByCapacity(Integer capacity) {
        return chickenHouseRepository.findByCapacity(capacity);
    }
    
    public List<ChickenHouse> findByCapacityBetween(Integer minCapacity, Integer maxCapacity) {
        return chickenHouseRepository.findByCapacityBetween(minCapacity, maxCapacity);
    }
    
    public List<ChickenHouse> findByAreaBetween(BigDecimal minArea, BigDecimal maxArea) {
        return chickenHouseRepository.findByAreaBetween(minArea, maxArea);
    }
    
    public List<ChickenHouse> findByIsActive(Boolean isActive) {
        return chickenHouseRepository.findByIsActive(isActive);
    }
    
    public List<ChickenHouse> findByIsActiveTrue() {
        return chickenHouseRepository.findByIsActiveTrue();
    }
    
    // ========== 自定义查询 ==========
    
    public List<ChickenHouse> findActiveHouses() {
        return chickenHouseRepository.findActiveHouses();
    }
    
    public Integer getTotalCapacity() {
        return chickenHouseRepository.getTotalCapacity();
    }
    
    // ========== 分页查询 ==========
    
    public Page<ChickenHouse> findByStatus(String status, Pageable pageable) {
        return chickenHouseRepository.findByStatus(status, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public Page<ChickenHouse> search(String status, Boolean isActive, Pageable pageable) {
        return chickenHouseRepository.search(status, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<ChickenHouse> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return chickenHouseRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return chickenHouseRepository.countByIsActiveTrue();
    }
    
    public long countByStatus(String status) {
        return chickenHouseRepository.countByStatus(status);
    }
    
    public long countByStatusAndIsActiveTrue(String status) {
        return chickenHouseRepository.countByStatusAndIsActiveTrue(status);
    }
    
    public BigDecimal getTotalArea() {
        return chickenHouseRepository.getTotalArea();
    }
    
    // ========== 保存和删除 ==========
    
    public ChickenHouse save(ChickenHouse house) {
        if (house.getCreatedTime() == null) {
            house.setCreatedTime(LocalDateTime.now());
        }
        return chickenHouseRepository.save(house);
    }
    
    public void deleteById(Long id) {
        chickenHouseRepository.findById(id).ifPresent(house -> {
            house.setIsActive(false);
            chickenHouseRepository.save(house);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByHouseNo(String houseNo) {
        return chickenHouseRepository.existsByHouseNo(houseNo);
    }
    
    public boolean existsByName(String name) {
        return chickenHouseRepository.existsByName(name);
    }
}