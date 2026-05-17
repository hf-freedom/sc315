package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Inventory {
    private Long id;
    private Long siteId;
    private Long vaccineId;
    private Long batchId;
    private Integer totalQuantity;
    private Integer reservedQuantity;
    private Integer availableQuantity;
    private LocalDateTime updateTime;
}
