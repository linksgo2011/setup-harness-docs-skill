package com.consultation.domain.datadictionary;

public interface DataDictionaryType {
    Class<? extends Description> getClazz();
    String getDescription();
}
