package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class User {
    private Long id;
    private String idCard;
    private String name;
    private String phone;
    private Integer riskLevel;
    private LocalDate birthDate;
    private String address;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
