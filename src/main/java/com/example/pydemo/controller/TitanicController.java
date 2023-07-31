package com.example.pydemo.controller;

import com.example.pydemo.model.Titanic;
import com.example.pydemo.service.TitanicService;
import com.example.pydemo.util.CsvUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@RequestMapping("/titanic")
public class TitanicController {
    @Autowired
    private TitanicService titanicService;

    @RequestMapping("/findAll")
    public List<Titanic> findAll() {
        return titanicService.findAll();
    }
}
