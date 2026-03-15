package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.Supplier;
import com.farm.finance.service.SupplierService;
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
@RequestMapping("/api/suppliers")
@RequiredArgsConstructor
public class SupplierController {
    
    private final SupplierService supplierService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<Supplier>> findAll() {
        return Result.success(supplierService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<Supplier> findById(@PathVariable Long id) {
        return supplierService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "供应商不存在"));
    }
    
    @GetMapping("/supplier-no/{supplierNo}")
    public Result<Supplier> findBySupplierNo(@PathVariable String supplierNo) {
        return supplierService.findBySupplierNo(supplierNo)
                .map(Result::success)
                .orElse(Result.error(404, "供应商不存在"));
    }
    
    @GetMapping("/name/{name}")
    public Result<Supplier> findByName(@PathVariable String name) {
        return supplierService.findByName(name)
                .map(Result::success)
                .orElse(Result.error(404, "供应商不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/contact-person/{contactPerson}")
    public Result<List<Supplier>> findByContactPerson(@PathVariable String contactPerson) {
        return Result.success(supplierService.findByContactPerson(contactPerson));
    }
    
    @GetMapping("/contact-person/search")
    public Result<List<Supplier>> findByContactPersonContaining(@RequestParam String keyword) {
        return Result.success(supplierService.findByContactPersonContaining(keyword));
    }
    
    @GetMapping("/phone/{phone}")
    public Result<Supplier> findByPhone(@PathVariable String phone) {
        return supplierService.findByPhone(phone)
                .map(Result::success)
                .orElse(Result.error(404, "供应商不存在"));
    }
    
    @GetMapping("/phone/search")
    public Result<List<Supplier>> findByPhoneContaining(@RequestParam String keyword) {
        return Result.success(supplierService.findByPhoneContaining(keyword));
    }
    
    @GetMapping("/name/search")
    public Result<List<Supplier>> findByNameContaining(@RequestParam String keyword) {
        return Result.success(supplierService.findByNameContaining(keyword));
    }
    
    @GetMapping("/active")
    public Result<List<Supplier>> findAllActive() {
        return Result.success(supplierService.findAllActive());
    }
    
    @GetMapping("/status/{isActive}")
    public Result<List<Supplier>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(supplierService.findByIsActive(isActive));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<Supplier>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(supplierService.findAll(pageable));
    }
    
    @GetMapping("/name/search/page")
    public Result<Page<Supplier>> findByNameContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(supplierService.findByNameContaining(keyword, pageable));
    }
    
    @GetMapping("/phone/search/page")
    public Result<Page<Supplier>> findByPhoneContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(supplierService.findByPhoneContaining(keyword, pageable));
    }
    
    @GetMapping("/contact-person/search/page")
    public Result<Page<Supplier>> findByContactPersonContainingPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(supplierService.findByContactPersonContaining(keyword, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<List<Supplier>> searchByName(@RequestParam String keyword) {
        return Result.success(supplierService.searchByName(keyword));
    }
    
    @GetMapping("/search/advanced")
    public Result<Page<Supplier>> searchAdvanced(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String contactPerson,
            @RequestParam(required = false) String phone,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(supplierService.search(name, contactPerson, phone, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<Supplier>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(supplierService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(supplierService.countByIsActiveTrue());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<Supplier> create(@RequestBody Supplier supplier) {
        if (supplier.getSupplierNo() != null && supplierService.existsBySupplierNo(supplier.getSupplierNo())) {
            return Result.error(409, "供应商编号已存在");
        }
        if (supplier.getPhone() != null && supplierService.existsByPhone(supplier.getPhone())) {
            return Result.error(409, "手机号已被使用");
        }
        return Result.success("创建成功", supplierService.save(supplier));
    }
    
    @PutMapping("/{id}")
    public Result<Supplier> update(@PathVariable Long id, @RequestBody Supplier supplier) {
        return supplierService.findById(id)
                .map(existing -> {
                    supplier.setId(id);
                    return Result.success("更新成功", supplierService.save(supplier));
                })
                .orElse(Result.error(404, "供应商不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (supplierService.findById(id).isEmpty()) {
            return Result.error(404, "供应商不存在");
        }
        supplierService.deleteById(id);
        return Result.success();
    }
}
