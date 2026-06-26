package com.consultation.domain.datadictionary;

import lombok.Builder;
import lombok.Value;
import java.util.List;

@Value @Builder
public class DataDictionary {
    String typeName;
    String description;
    List<Item> items;

    @Value @Builder
    public static class Item {
        String code;
        String description;
    }
}
