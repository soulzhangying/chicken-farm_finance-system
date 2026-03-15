package com.farm.finance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "inventory_transaction")
public class InventoryTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_no", nullable = false, unique = true, length = 20)
    private String transactionNo;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "batch_no", length = 20)
    private String batchNo;

    @Column(name = "quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;

    @Column(name = "unit_price", precision = 6, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "price_type", length = 20)
    private String priceType;

    @Column(name = "before_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal beforeQuantity;

    @Column(name = "after_quantity", nullable = false, precision = 10, scale = 2)
    private BigDecimal afterQuantity;

    @Column(name = "reference_id", length = 50)
    private String referenceId;

    @Column(name = "reference_type", length = 20)
    private String referenceType;

    @Column(name = "related_money_id")
    private Long relatedMoneyId;

    @Column(name = "reason", length = 50)
    private String reason;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
}
