package com.farm.finance.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 库存预警DTO
 */
@Data
public class InventoryAlertDTO {
    private Long productId;
    private String productNo;
    private String productName;
    private String productType;
    private String unit;
    private BigDecimal currentQuantity;     // 当前库存
    private BigDecimal minQuantity;         // 最低库存阈值
    private String alertLevel;              // 预警级别: LOW-库存不足, CRITICAL-库存告急
    private String alertMessage;            // 预警信息
}
