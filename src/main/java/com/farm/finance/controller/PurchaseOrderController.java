package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.PurchaseOrder;
import com.farm.finance.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {
    
    private final PurchaseOrderService purchaseOrderService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<PurchaseOrder>> findAll() {
        return Result.success(purchaseOrderService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<PurchaseOrder> findById(@PathVariable Long id) {
        return purchaseOrderService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "采购订单不存在"));
    }
    
    @GetMapping("/order-no/{orderNo}")
    public Result<PurchaseOrder> findByOrderNo(@PathVariable String orderNo) {
        return purchaseOrderService.findByOrderNo(orderNo)
                .map(Result::success)
                .orElse(Result.error(404, "采购订单不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/supplier/{supplierId}")
    public Result<List<PurchaseOrder>> findBySupplierId(@PathVariable Long supplierId) {
        return Result.success(purchaseOrderService.findBySupplierId(supplierId));
    }
    
    @GetMapping("/created-by/{createdBy}")
    public Result<List<PurchaseOrder>> findByCreatedBy(@PathVariable Long createdBy) {
        return Result.success(purchaseOrderService.findByCreatedBy(createdBy));
    }
    
    @GetMapping("/payment-status/{paymentStatus}")
    public Result<List<PurchaseOrder>> findByPaymentStatus(@PathVariable String paymentStatus) {
        return Result.success(purchaseOrderService.findByPaymentStatus(paymentStatus));
    }
    
    @GetMapping("/delivery-status/{deliveryStatus}")
    public Result<List<PurchaseOrder>> findByDeliveryStatus(@PathVariable String deliveryStatus) {
        return Result.success(purchaseOrderService.findByDeliveryStatus(deliveryStatus));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<PurchaseOrder>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(purchaseOrderService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<PurchaseOrder>> findByIsActiveTrue() {
        return Result.success(purchaseOrderService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/order-date")
    public Result<List<PurchaseOrder>> findByOrderDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return Result.success(purchaseOrderService.findByOrderDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<PurchaseOrder>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(purchaseOrderService.findAll(pageable));
    }
    
    @GetMapping("/supplier/{supplierId}/page")
    public Result<Page<PurchaseOrder>> findBySupplierIdPage(
            @PathVariable Long supplierId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(purchaseOrderService.findBySupplierId(supplierId, pageable));
    }
    
    @GetMapping("/payment-status/{paymentStatus}/page")
    public Result<Page<PurchaseOrder>> findByPaymentStatusPage(
            @PathVariable String paymentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(purchaseOrderService.findByPaymentStatus(paymentStatus, pageable));
    }
    
    @GetMapping("/delivery-status/{deliveryStatus}/page")
    public Result<Page<PurchaseOrder>> findByDeliveryStatusPage(
            @PathVariable String deliveryStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(purchaseOrderService.findByDeliveryStatus(deliveryStatus, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<PurchaseOrder>> search(
            @RequestParam(required = false) Long supplierId,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String deliveryStatus,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(purchaseOrderService.search(supplierId, paymentStatus, deliveryStatus, isActive, pageable));
    }
    
    @GetMapping("/search/keyword")
    public Result<List<PurchaseOrder>> searchByKeyword(@RequestParam String keyword) {
        return Result.success(purchaseOrderService.searchByKeyword(keyword));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<PurchaseOrder>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(purchaseOrderService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(purchaseOrderService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/payment-status/{paymentStatus}")
    public Result<Long> countByPaymentStatus(@PathVariable String paymentStatus) {
        return Result.success(purchaseOrderService.countByPaymentStatus(paymentStatus));
    }
    
    @GetMapping("/count/delivery-status/{deliveryStatus}")
    public Result<Long> countByDeliveryStatus(@PathVariable String deliveryStatus) {
        return Result.success(purchaseOrderService.countByDeliveryStatus(deliveryStatus));
    }
    
    @GetMapping("/total/amount")
    public Result<BigDecimal> sumTotalAmount() {
        return Result.success(purchaseOrderService.sumTotalAmount());
    }
    
    @GetMapping("/payment-status/{paymentStatus}/sum")
    public Result<BigDecimal> sumAmountByPaymentStatus(@PathVariable String paymentStatus) {
        return Result.success(purchaseOrderService.sumAmountByPaymentStatus(paymentStatus));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<PurchaseOrder> create(@RequestBody PurchaseOrder order) {
        if (order.getOrderNo() != null && purchaseOrderService.existsByOrderNo(order.getOrderNo())) {
            return Result.error(409, "订单编号已存在");
        }
        return Result.success("创建成功", purchaseOrderService.save(order));
    }
    
    @PutMapping("/{id}")
    public Result<PurchaseOrder> update(@PathVariable Long id, @RequestBody PurchaseOrder order) {
        return purchaseOrderService.findById(id)
                .map(existing -> {
                    order.setId(id);
                    return Result.success("更新成功", purchaseOrderService.save(order));
                })
                .orElse(Result.error(404, "采购订单不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (purchaseOrderService.findById(id).isEmpty()) {
            return Result.error(404, "采购订单不存在");
        }
        purchaseOrderService.deleteById(id);
        return Result.success();
    }
}
