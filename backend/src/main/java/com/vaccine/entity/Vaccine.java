package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Vaccine {
    private Long id;
    private String name;
    private String manufacturer;
    private Integer dosesRequired;
    private Integer intervalDays;
    private String description;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
