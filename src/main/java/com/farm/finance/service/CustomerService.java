package com.farm.finance.service;

import com.farm.finance.entity.Customer;
import com.farm.finance.repository.CustomerRepository;
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
public class CustomerService {
    
    private final CustomerRepository customerRepository;
    
    // ========== 基本查询 ==========
    
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }
    
    public Page<Customer> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable);
    }
    
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    
    public Optional<Customer> findByCustomerNo(String customerNo) {
        return customerRepository.findByCustomerNo(customerNo);
    }
    
    public Optional<Customer> findByName(String name) {
        return customerRepository.findByName(name);
    }
    
    // ========== 按字段查询 ==========
    
    public List<Customer> findByPhone(String phone) {
        return customerRepository.findByPhone(phone);
    }
    
    public List<Customer> findByPhoneContaining(String phone) {
        return customerRepository.findByPhoneContaining(phone);
    }
    
    public List<Customer> findByAddressContaining(String address) {
        return customerRepository.findByAddressContaining(address);
    }
    
    public List<Customer> findByIsActive(Boolean isActive) {
        return customerRepository.findByIsActive(isActive);
    }
    
    public List<Customer> findByIsActiveTrue() {
        return customerRepository.findByIsActiveTrue();
    }
    
    public List<Customer> findAllActive() {
        return customerRepository.findAllActive();
    }
    
    // ========== 分页查询 ==========
    
    public Page<Customer> findByNameContaining(String name, Pageable pageable) {
        return customerRepository.findByNameContaining(name, pageable);
    }
    
    public Page<Customer> findByPhoneContaining(String phone, Pageable pageable) {
        return customerRepository.findByPhoneContaining(phone, pageable);
    }
    
    // ========== 组合搜索 ==========
    
    public List<Customer> searchByName(String keyword) {
        return customerRepository.searchByName(keyword);
    }
    
    public Page<Customer> search(String name, String phone, String address, 
                                  Boolean isActive, Pageable pageable) {
        return customerRepository.search(name, phone, address, isActive, pageable);
    }
    
    // ========== 时间范围查询 ==========
    
    public List<Customer> findByCreatedTimeBetween(LocalDateTime startTime, LocalDateTime endTime) {
        return customerRepository.findByCreatedTimeBetween(startTime, endTime);
    }
    
    // ========== 统计 ==========
    
    public long countByIsActiveTrue() {
        return customerRepository.countByIsActiveTrue();
    }
    
    // ========== 保存和删除 ==========
    
    public Customer save(Customer customer) {
        if (customer.getCreatedTime() == null) {
            customer.setCreatedTime(LocalDateTime.now());
        }
        return customerRepository.save(customer);
    }
    
    public void deleteById(Long id) {
        customerRepository.findById(id).ifPresent(customer -> {
            customer.setIsActive(false);
            customerRepository.save(customer);
        });
    }
    
    // ========== 存在性检查 ==========
    
    public boolean existsByCustomerNo(String customerNo) {
        return customerRepository.findByCustomerNo(customerNo).isPresent();
    }
    
    public boolean existsByPhone(String phone) {
        return customerRepository.existsByPhone(phone);
    }
    
    // ========== 关键词搜索 ==========
    
    public List<Customer> searchByKeyword(String keyword) {
        return customerRepository.searchByKeyword(keyword);
    }
}