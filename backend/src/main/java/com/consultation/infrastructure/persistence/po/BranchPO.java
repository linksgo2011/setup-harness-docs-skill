package com.consultation.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("branch")
public class BranchPO {
    private String id; private String name; private String address; private String phone;
    private String status;
    private String createdBy; private LocalDateTime createdTime;
    private String updatedBy; private LocalDateTime updatedTime;
}
