package com.farm.finance.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "death_record")
public class DeathRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "house_id", nullable = false)
    private Long houseId;

    @Column(name = "death_date", nullable = false)
    private LocalDate deathDate;

    @Column(name = "death_count", nullable = false)
    private Integer deathCount;

    @Column(name = "death_reason", length = 100)
    private String deathReason;

    @Column(name = "operator_id", nullable = false)
    private Long operatorId;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @Column(name = "created_time", nullable = false, updatable = false)
    private LocalDateTime createdTime;
}