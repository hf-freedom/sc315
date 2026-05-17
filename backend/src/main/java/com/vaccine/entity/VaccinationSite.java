package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class VaccinationSite {
    private Long id;
    private String name;
    private String address;
    private String contact;
    private String phone;
    private String workHours;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
