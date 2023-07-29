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
        ArrayList<Titanic> titanicArrayList = (ArrayList<Titanic>) titanicService.findAll();

        String rootPath = "src/main/resources/";
        String filePath = rootPath + "csv-file";
        String fileName = "csv_file_";

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");
        String[] fileds = new String[]{"PassengerId", "Name"};// 设置列英文名,即实体类里面对应的列名

        CsvUtil<Titanic> csvUtil = new CsvUtil<>();

        String splitStr = "@@@";
        String resultFilename = csvUtil.createCSVFile(titanicArrayList, fileds, map, filePath, fileName, splitStr);
        System.out.println(resultFilename);

        return titanicArrayList;
    }
}
