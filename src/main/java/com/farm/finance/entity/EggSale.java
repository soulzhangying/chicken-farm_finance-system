package com.farm.finance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "egg_sale")
public class EggSale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sale_no", nullable = false, unique = true, length = 20)
    private String saleNo;

    @Column(name = "sale_date", nullable = false)
    private LocalDate saleDate;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "sale_quantity", nullable = false)
    private Integer saleQuantity;

    @Column(name = "sale_weight", precision = 10, scale = 2)
    private BigDecimal saleWeight;

    @Column(name = "unit_price", precision = 6, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "remarks", length = 200)
    private String remarks;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
}
