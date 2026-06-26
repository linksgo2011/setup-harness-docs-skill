package com.consultation.application.service;

import com.consultation.domain.datadictionary.DataDictionary;
import com.consultation.domain.datadictionary.DataDictionaryUtil;
import com.consultation.domain.datadictionary.DemoDataDictionaryType;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DataDictionaryAppService {

    public List<DataDictionary> getAll() {
        return DataDictionaryUtil.allDictionaries(DemoDataDictionaryType.values());
    }

    public DataDictionary getByType(String typeName) {
        return Arrays.stream(DemoDataDictionaryType.values())
                .filter(t -> t.name().equalsIgnoreCase(typeName))
                .findFirst()
                .map(DataDictionaryUtil::toDataDictionary)
                .orElseThrow(() -> new IllegalArgumentException("字典类型不存在: " + typeName));
    }
}
