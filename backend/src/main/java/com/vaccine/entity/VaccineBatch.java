package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class VaccineBatch {
    private Long id;
    private Long vaccineId;
    private String batchNo;
    private LocalDate productionDate;
    private LocalDate expireDate;
    private Integer totalQuantity;
    private Integer availableQuantity;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
