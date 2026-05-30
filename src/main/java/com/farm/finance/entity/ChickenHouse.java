package com.farm.finance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "chicken_house")
public class ChickenHouse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_no", nullable = false, unique = true, length = 20)
    private String houseNo;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @Column(name = "area", precision = 10, scale = 2)
    private BigDecimal area;

    // ========== 批次相关字段（原ChickenBatch字段合并到此） ==========
    
    @Column(name = "group_name", length = 50)
    private String groupName;

    @Column(name = "breed", length = 50)
    private String breed;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "entry_date")
    private LocalDate entryDate;

    @Column(name = "entry_quantity")
    private Integer entryQuantity;

    @Column(name = "entry_price", precision = 6, scale = 2)
    private BigDecimal entryPrice;

    @Column(name = "entry_total_cost", precision = 10, scale = 2)
    private BigDecimal entryTotalCost;

    @Column(name = "current_quantity")
    private Integer currentQuantity;

    @Column(name = "current_age")
    private Integer currentAge = 0;

    @Column(name = "current_weight", precision = 8, scale = 2)
    private BigDecimal currentWeight;

    @Column(name = "expected_sale_date")
    private LocalDate expectedSaleDate;

    @Column(name = "actual_sale_date")
    private LocalDate actualSaleDate;

    @Column(name = "total_death_count", nullable = false)
    private Integer totalDeathCount = 0;

    @Column(name = "remarks", length = 500)
    private String remarks;

    // ========== 基础字段 ==========
    
    @Column(name = "status", nullable = false, length = 20)
    private String status = "EMPTY";  // EMPTY-空闲, ACTIVE-养殖中, FINISHED-已出栏

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
}