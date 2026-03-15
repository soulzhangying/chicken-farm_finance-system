package com.farm.finance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chicken_batch")
public class ChickenBatch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "batch_no", nullable = false, unique = true, length = 20)
    private String batchNo;

    @Column(name = "house_id", nullable = false)
    private Long houseId;

    @Column(name = "group_name", nullable = false, length = 50)
    private String groupName;

    @Column(name = "breed", length = 50)
    private String breed;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "entry_quantity", nullable = false)
    private Integer entryQuantity;

    @Column(name = "entry_price", precision = 6, scale = 2)
    private BigDecimal entryPrice;

    @Column(name = "entry_total_cost", precision = 10, scale = 2)
    private BigDecimal entryTotalCost;

    @Column(name = "current_quantity", nullable = false)
    private Integer currentQuantity;

    @Column(name = "current_age", nullable = false)
    private Integer currentAge = 0;

    @Column(name = "current_weight", precision = 8, scale = 2)
    private BigDecimal currentWeight;

    @Column(name = "expected_sale_date")
    private LocalDate expectedSaleDate;

    @Column(name = "actual_sale_date")
    private LocalDate actualSaleDate;

    @Column(name = "total_feed_cost", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalFeedCost = BigDecimal.ZERO;

    @Column(name = "total_death_count", nullable = false)
    private Integer totalDeathCount = 0;

    @Column(name = "total_egg_count", nullable = false)
    private Integer totalEggCount = 0;

    @Column(name = "status", nullable = false, length = 20)
    private String status = "ACTIVE";

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
}
