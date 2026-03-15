package com.farm.finance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "finance_record")
public class FinanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_no", nullable = false, unique = true, length = 20)
    private String recordNo;

    @Column(name = "record_date", nullable = false)
    private LocalDate recordDate;

    @Column(name = "money_type", nullable = false, length = 20)
    private String moneyType;

    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "cost_type", length = 20)
    private String costType;

    @Column(name = "income_type", length = 20)
    private String incomeType;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "batch_no", length = 20)
    private String batchNo;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    @Column(name = "reference_id", length = 50)
    private String referenceId;

    @Column(name = "reference_type", length = 20)
    private String referenceType;

    @Column(name = "description", length = 200)
    private String description;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
}