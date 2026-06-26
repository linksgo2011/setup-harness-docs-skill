package com.consultation.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.consultation.infrastructure.persistence.po.ConsultantPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConsultantMapper extends BaseMapper<ConsultantPO> {}
