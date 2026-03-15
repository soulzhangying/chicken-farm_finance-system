package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.Product;
import com.farm.finance.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<Product>> findAll() {
        return Result.success(productService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<Product> findById(@PathVariable Long id) {
        return productService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "产品不存在"));
    }
    
    @GetMapping("/product-no/{productNo}")
    public Result<Product> findByProductNo(@PathVariable String productNo) {
        return productService.findByProductNo(productNo)
                .map(Result::success)
                .orElse(Result.error(404, "产品不存在"));
    }
    
    @GetMapping("/name/{name}")
    public Result<Product> findByName(@PathVariable String name) {
        return productService.findByName(name)
                .map(Result::success)
                .orElse(Result.error(404, "产品不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/type/{productType}")
    public Result<List<Product>> findByProductType(@PathVariable String productType) {
        return Result.success(productService.findByProductType(productType));
    }
    
    @GetMapping("/type/{productType}/active")
    public Result<List<Product>> findByProductTypeAndActive(@PathVariable String productType) {
        return Result.success(productService.findByProductTypeAndIsActiveTrue(productType));
    }
    
    @GetMapping("/unit/{unit}")
    public Result<List<Product>> findByUnit(@PathVariable String unit) {
        return Result.success(productService.findByUnit(unit));
    }
    
    @GetMapping("/active")
    public Result<List<Product>> findAllActive() {
        return Result.success(productService.findAllActive());
    }
    
    @GetMapping("/active/type/{type}")
    public Result<List<Product>> findActiveByType(@PathVariable String type) {
        return Result.success(productService.findActiveByType(type));
    }
    
    @GetMapping("/status/{isActive}")
    public Result<List<Product>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(productService.findByIsActive(isActive));
    }
    
    // ========== 价格范围查询 ==========
    
    @GetMapping("/sale-price")
    public Result<List<Product>> findBySalePriceBetween(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return Result.success(productService.findBySalePriceBetween(minPrice, maxPrice));
    }
    
    @GetMapping("/purchase-price")
    public Result<List<Product>> findByPurchasePriceBetween(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        return Result.success(productService.findByPurchasePriceBetween(minPrice, maxPrice));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<Product>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(productService.findAll(pageable));
    }
    
    @GetMapping("/type/{productType}/page")
    public Result<Page<Product>> findByProductTypePage(
            @PathVariable String productType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(productService.findByProductType(productType, pageable));
    }
    
    @GetMapping("/unit/{unit}/page")
    public Result<Page<Product>> findByUnitPage(
            @PathVariable String unit,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(productService.findByUnit(unit, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<Product>> search(
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(productService.search(productType, isActive, pageable));
    }
    
    @GetMapping("/search/price")
    public Result<Page<Product>> searchWithPrice(
            @RequestParam(required = false) String productType,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(productService.searchWithPrice(productType, minPrice, maxPrice, isActive, pageable));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<Product>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(productService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(productService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/type/{productType}")
    public Result<Long> countByProductType(@PathVariable String productType) {
        return Result.success(productService.countByProductType(productType));
    }
    
    @GetMapping("/count/type/{productType}/active")
    public Result<Long> countByProductTypeAndActive(@PathVariable String productType) {
        return Result.success(productService.countByProductTypeAndIsActiveTrue(productType));
    }
    
    @GetMapping("/count/group-by-type")
    public Result<List<Object[]>> countGroupByProductType() {
        return Result.success(productService.countGroupByProductType());
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<Product> create(@RequestBody Product product) {
        if (product.getProductNo() != null && productService.existsByProductNo(product.getProductNo())) {
            return Result.error(409, "产品编号已存在");
        }
        if (product.getName() != null && productService.existsByName(product.getName())) {
            return Result.error(409, "产品名称已存在");
        }
        return Result.success("创建成功", productService.save(product));
    }
    
    @PutMapping("/{id}")
    public Result<Product> update(@PathVariable Long id, @RequestBody Product product) {
        return productService.findById(id)
                .map(existing -> {
                    product.setId(id);
                    return Result.success("更新成功", productService.save(product));
                })
                .orElse(Result.error(404, "产品不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (productService.findById(id).isEmpty()) {
            return Result.error(404, "产品不存在");
        }
        productService.deleteById(id);
        return Result.success();
    }
}