package com.consultation.adapter.rest;

import com.consultation.application.service.DataDictionaryAppService;
import com.consultation.domain.datadictionary.DataDictionary;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/data-dictionaries")
@RequiredArgsConstructor
public class DataDictionaryController {

    private final DataDictionaryAppService service;

    @GetMapping
    public List<DataDictionary> list() {
        return service.getAll();
    }

    @GetMapping("/{type}")
    public DataDictionary getByType(@PathVariable String type) {
        return service.getByType(type);
    }
}
