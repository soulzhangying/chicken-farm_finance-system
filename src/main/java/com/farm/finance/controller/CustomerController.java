package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.Customer;
import com.farm.finance.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<Customer>> findAll() {
        return Result.success(customerService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<Customer> findById(@PathVariable Long id) {
        return customerService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "客户不存在"));
    }
    
    @GetMapping("/customer-no/{customerNo}")
    public Result<Customer> findByCustomerNo(@PathVariable String customerNo) {
        return customerService.findByCustomerNo(customerNo)
                .map(Result::success)
                .orElse(Result.error(404, "客户不存在"));
    }
    
    @GetMapping("/name/{name}")
    public Result<Customer> findByName(@PathVariable String name) {
        return customerService.findByName(name)
                .map(Result::success)
                .orElse(Result.error(404, "客户不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/phone/{phone}")
    public Result<List<Customer>> findByPhone(@PathVariable String phone) {
        return Result.success(customerService.findByPhone(phone));
    }
    
    @GetMapping("/phone/search")
    public Result<List<Customer>> findByPhoneContaining(@RequestParam String keyword) {
        return Result.success(customerService.findByPhoneContaining(keyword));
    }
    
    @GetMapping("/address/search")
    public Result<List<Customer>> findByAddressContaining(@RequestParam String keyword) {
        return Result.success(customerService.findByAddressContaining(keyword));
    }
    
    @GetMapping("/active")
    public Result<List<Customer>> findAllActive() {
        return Result.success(customerService.findAllActive());
    }
    
    @GetMapping("/status/{isActive}")
    public Result<List<Customer>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(customerService.findByIsActive(isActive));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<Customer>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(customerService.findAll(pageable));
    }
    
    @GetMapping("/name/search/page")
    public Result<Page<Customer>> findByNameContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(customerService.findByNameContaining(keyword, pageable));
    }
    
    @GetMapping("/phone/search/page")
    public Result<Page<Customer>> findByPhoneContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(customerService.findByPhoneContaining(keyword, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<List<Customer>> searchByName(@RequestParam String keyword) {
        return Result.success(customerService.searchByName(keyword));
    }
    
    @GetMapping("/search/advanced")
    public Result<Page<Customer>> searchAdvanced(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(customerService.search(name, phone, address, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<Customer>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(customerService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(customerService.countByIsActiveTrue());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<Customer> create(@RequestBody Customer customer) {
        if (customer.getCustomerNo() != null && customerService.existsByCustomerNo(customer.getCustomerNo())) {
            return Result.error(409, "客户编号已存在");
        }
        if (customer.getPhone() != null && customerService.existsByPhone(customer.getPhone())) {
            return Result.error(409, "手机号已被使用");
        }
        return Result.success("创建成功", customerService.save(customer));
    }
    
    @PutMapping("/{id}")
    public Result<Customer> update(@PathVariable Long id, @RequestBody Customer customer) {
        return customerService.findById(id)
                .map(existing -> {
                    customer.setId(id);
                    return Result.success("更新成功", customerService.save(customer));
                })
                .orElse(Result.error(404, "客户不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (customerService.findById(id).isEmpty()) {
            return Result.error(404, "客户不存在");
        }
        customerService.deleteById(id);
        return Result.success();
    }
}
