package com.vaccine.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Transfer {
    private Long id;
    private Long fromSiteId;
    private Long toSiteId;
    private Long vaccineId;
    private Long batchId;
    private Integer quantity;
    private Integer status;
    private String remark;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
