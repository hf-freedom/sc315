package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Reservation {
    private Long id;
    private Long userId;
    private Long siteId;
    private Long vaccineId;
    private Long batchId;
    private LocalDate reservationDate;
    private String timeSlot;
    private Integer status;
    private Integer doseNumber;
    private LocalDate nextSuggestDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
