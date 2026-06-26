package com.consultation.infrastructure.persistence.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user")
public class UserPO {
    private String id; private String email; private String password; private String name;
    private String phone; private String role; private String status; private Integer deleted;
    private String createdBy; private LocalDateTime createdTime;
    private String updatedBy; private LocalDateTime updatedTime;
}
