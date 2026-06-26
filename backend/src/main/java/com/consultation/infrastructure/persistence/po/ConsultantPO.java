package com.consultation.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("consultant")
public class ConsultantPO {
    private String id; private String name; private String title; private String bio;
    private String specialties; private String avatar; private String branchId;
    private String status; private Integer sortOrder;
    private String createdBy; private LocalDateTime createdTime;
    private String updatedBy; private LocalDateTime updatedTime;
}
