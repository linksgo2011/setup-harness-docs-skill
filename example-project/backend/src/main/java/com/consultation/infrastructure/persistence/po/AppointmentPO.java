package com.consultation.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@TableName("appointment")
public class AppointmentPO {
    private String id; private String userId; private String consultantId; private String branchId;
    private LocalDate appointmentDate; private String startTime; private String endTime;
    private Integer durationMinutes; private String status; private String note; private String cancelReason;
    private String createdBy; private LocalDateTime createdTime;
    private String updatedBy; private LocalDateTime updatedTime;
}
