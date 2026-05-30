package com.farm.finance.controller;

import com.farm.finance.common.Result;
import com.farm.finance.entity.SalesOrder;
import com.farm.finance.service.SalesOrderService;
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
@RequestMapping("/api/sales-orders")
@RequiredArgsConstructor
public class SalesOrderController {
    
    private final SalesOrderService salesOrderService;
    
    // ========== 基本查询 ==========
    
    @GetMapping
    public Result<List<SalesOrder>> findAll() {
        return Result.success(salesOrderService.findAll());
    }
    
    @GetMapping("/{id}")
    public Result<SalesOrder> findById(@PathVariable Long id) {
        return salesOrderService.findById(id)
                .map(Result::success)
                .orElse(Result.error(404, "销售订单不存在"));
    }
    
    @GetMapping("/order-no/{orderNo}")
    public Result<SalesOrder> findByOrderNo(@PathVariable String orderNo) {
        return salesOrderService.findByOrderNo(orderNo)
                .map(Result::success)
                .orElse(Result.error(404, "销售订单不存在"));
    }
    
    // ========== 按字段查询 ==========
    
    @GetMapping("/customer/{customerId}")
    public Result<List<SalesOrder>> findByCustomerId(@PathVariable Long customerId) {
        return Result.success(salesOrderService.findByCustomerId(customerId));
    }
    
    @GetMapping("/payment-status/{paymentStatus}")
    public Result<List<SalesOrder>> findByPaymentStatus(@PathVariable String paymentStatus) {
        return Result.success(salesOrderService.findByPaymentStatus(paymentStatus));
    }
    
    @GetMapping("/order-status/{orderStatus}")
    public Result<List<SalesOrder>> findByOrderStatus(@PathVariable String orderStatus) {
        return Result.success(salesOrderService.findByOrderStatus(orderStatus));
    }
    
    @GetMapping("/delivery-type/{deliveryType}")
    public Result<List<SalesOrder>> findByDeliveryType(@PathVariable String deliveryType) {
        return Result.success(salesOrderService.findByDeliveryType(deliveryType));
    }
    
    @GetMapping("/is-active/{isActive}")
    public Result<List<SalesOrder>> findByIsActive(@PathVariable Boolean isActive) {
        return Result.success(salesOrderService.findByIsActive(isActive));
    }
    
    @GetMapping("/active")
    public Result<List<SalesOrder>> findByIsActiveTrue() {
        return Result.success(salesOrderService.findByIsActiveTrue());
    }
    
    // ========== 日期范围查询 ==========
    
    @GetMapping("/order-date")
    public Result<List<SalesOrder>> findByOrderDateBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return Result.success(salesOrderService.findByOrderDateBetween(startDate, endDate));
    }
    
    // ========== 分页查询 ==========
    
    @GetMapping("/page")
    public Result<Page<SalesOrder>> findAllPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(salesOrderService.findAll(pageable));
    }
    
    @GetMapping("/customer/{customerId}/page")
    public Result<Page<SalesOrder>> findByCustomerIdPage(
            @PathVariable Long customerId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(salesOrderService.findByCustomerId(customerId, pageable));
    }
    
    @GetMapping("/payment-status/{paymentStatus}/page")
    public Result<Page<SalesOrder>> findByPaymentStatusPage(
            @PathVariable String paymentStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(salesOrderService.findByPaymentStatus(paymentStatus, pageable));
    }
    
    @GetMapping("/order-status/{orderStatus}/page")
    public Result<Page<SalesOrder>> findByOrderStatusPage(
            @PathVariable String orderStatus,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return Result.success(salesOrderService.findByOrderStatus(orderStatus, pageable));
    }
    
    // ========== 组合搜索 ==========
    
    @GetMapping("/search")
    public Result<Page<SalesOrder>> search(
            @RequestParam(required = false) Long customerId,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String orderStatus,
            @RequestParam(required = false) Boolean isActive,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {
        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return Result.success(salesOrderService.search(customerId, paymentStatus, orderStatus, isActive, pageable));
    }
    
    @GetMapping("/search/keyword")
    public Result<List<SalesOrder>> searchByKeyword(@RequestParam String keyword) {
        return Result.success(salesOrderService.searchByKeyword(keyword));
    }
    
    // ========== 时间范围查询 ==========
    
    @GetMapping("/created-time")
    public Result<List<SalesOrder>> findByCreatedTimeBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        return Result.success(salesOrderService.findByCreatedTimeBetween(startTime, endTime));
    }
    
    // ========== 统计 ==========
    
    @GetMapping("/count/active")
    public Result<Long> countByIsActiveTrue() {
        return Result.success(salesOrderService.countByIsActiveTrue());
    }
    
    @GetMapping("/count/payment-status/{paymentStatus}")
    public Result<Long> countByPaymentStatus(@PathVariable String paymentStatus) {
        return Result.success(salesOrderService.countByPaymentStatus(paymentStatus));
    }
    
    @GetMapping("/count/order-status/{orderStatus}")
    public Result<Long> countByOrderStatus(@PathVariable String orderStatus) {
        return Result.success(salesOrderService.countByOrderStatus(orderStatus));
    }
    
    @GetMapping("/total/amount")
    public Result<BigDecimal> sumTotalAmount() {
        return Result.success(salesOrderService.sumTotalAmount());
    }
    
    @GetMapping("/total/actual-amount")
    public Result<BigDecimal> sumActualAmount() {
        return Result.success(salesOrderService.sumActualAmount());
    }
    
    @GetMapping("/payment-status/{paymentStatus}/sum")
    public Result<BigDecimal> sumAmountByPaymentStatus(@PathVariable String paymentStatus) {
        return Result.success(salesOrderService.sumAmountByPaymentStatus(paymentStatus));
    }
    
    // ========== 创建和更新 ==========
    
    @PostMapping
    public Result<SalesOrder> create(@RequestBody SalesOrder order) {
        if (order.getOrderNo() != null && salesOrderService.existsByOrderNo(order.getOrderNo())) {
            return Result.error(409, "订单编号已存在");
        }
        return Result.success("创建成功", salesOrderService.save(order));
    }
    
    @PutMapping("/{id}")
    public Result<SalesOrder> update(@PathVariable Long id, @RequestBody SalesOrder order) {
        return salesOrderService.findById(id)
                .map(existing -> {
                    order.setId(id);
                    return Result.success("更新成功", salesOrderService.save(order));
                })
                .orElse(Result.error(404, "销售订单不存在"));
    }
    
    // ========== 删除 ==========
    
    @DeleteMapping("/{id}")
    public Result<Void> delete(@PathVariable Long id) {
        if (salesOrderService.findById(id).isEmpty()) {
            return Result.error(404, "销售订单不存在");
        }
        salesOrderService.deleteById(id);
        return Result.success();
    }
}
