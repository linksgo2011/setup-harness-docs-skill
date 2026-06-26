package com.consultation.domain.datadictionary;

import java.util.Arrays;
import java.util.List;

public final class DataDictionaryUtil {

    private DataDictionaryUtil() {}

    public static DataDictionary toDataDictionary(DataDictionaryType type) {
        var items = Arrays.stream(type.getClazz().getEnumConstants())
                .map(e -> DataDictionary.Item.builder().code(e.getCode()).description(e.getDescription()).build())
                .toList();
        return DataDictionary.builder().typeName(((Enum<?>) type).name()).description(type.getDescription()).items(items).build();
    }

    public static List<DataDictionary> allDictionaries(DataDictionaryType[] types) {
        return Arrays.stream(types).map(DataDictionaryUtil::toDataDictionary).toList();
    }
}
