package com.example.pydemo;

import com.example.pydemo.model.Titanic;
import com.example.pydemo.service.TitanicService;
import com.example.pydemo.util.CsvUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootContextLoader;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@SpringBootTest
class PydemoApplicationTests {
    @Autowired
    private TitanicService titanicService;

    @Test
    void contextLoads() {
    }

    @Test
    void RunPy() {
        System.out.println("Start");
        // python脚本的绝对路径，在windows中用"\\"分隔，在Linux中用"/"分隔
        String pyPath = "./src/main/resources/py-file/plus.py";

        // 传入python脚本的参数为”111“
        String[] args1 = new String[]{"python", pyPath, "111 222"};

        try {
            // 执行Python文件，并传入参数
            Process process = Runtime.getRuntime().exec(args1);
            // 获取Python输出字符串作为输入流被Java读取
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String actionStr = in.readLine();
            if (actionStr != null) {
                System.out.println(actionStr);
            }

            in.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End");
    }

    @Test
    public void findAll() {
        List<Titanic> titanicList = titanicService.findAll();
        for (Titanic titanic : titanicList) {
            System.out.println(titanic.toString());
        }
    }

    @Test
    public void createCSVFile() {

        ArrayList<Titanic> titanicArrayList = (ArrayList<Titanic>) titanicService.findAll();

        String rootPath = "src/main/resources/";
        String filePath = rootPath + "py-file";
        String fileName = "csv_file_";


        String sourceColumn = "Id,Pclass,Sex";
        String targetColumn = "Age";

        String regex = ",";
        String[] fields = stringToArray(sourceColumn + regex + targetColumn, ",");

        System.out.println(Arrays.toString(fields));

        HashMap<String, String> map = arrayToHashMap(fields);


        CsvUtil<Titanic> csvUtil = new CsvUtil<>();

        String splitStr = "@@@";
        String resultFilename = csvUtil.createCSVFile(titanicArrayList, fields, map, filePath, fileName, splitStr);

        for (Titanic titanic : titanicArrayList) {
            System.out.println(titanic.toString());
        }

        System.out.println("Start Python");
        // python解释器的路径
        String pyExecPath = "/Users/garlicv/anaconda3/envs/py3.7/bin/python";
        // python脚本的路径，
        String pyPath = "src/main/resources/py-file/deficiency.py";
        String targetPath = filePath + "/";
        String cleanFilename = "result.csv";
        // python 执行命令行
        String execSh = pyExecPath +
                " " +
                pyPath +
                "  --file_path  " +
                targetPath + resultFilename +
                "  --result_file_path  " +
                targetPath + cleanFilename +
                "  --sep  " +
                splitStr +
                "  --source  " +
                sourceColumn +
                "  --target  " +
                targetColumn;

        System.out.println(execSh);

        try {
            // 执行Python文件，并传入参数
            Process process = Runtime.getRuntime().exec(execSh);
            // 获取Python输出字符串作为输入流被Java读取
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String actionStr = in.readLine();
            if (actionStr != null) {
                System.out.println(actionStr);
            }
            in.close();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("End Python");

        CsvUtil.deleteFile(filePath, resultFilename);


    }

    public static String[] stringToArray(String sourceColumn, String regex) {
        // Split the sourceColumn string by commas
        String[] result = sourceColumn.split(regex);

        // Trim any leading or trailing whitespaces in each element
        for (int i = 0; i < result.length; i++) {
            result[i] = result[i].trim();
        }

        return result;
    }

    public static HashMap<String, String> arrayToHashMap(String[] array) {
        HashMap<String, String> hashMap = new HashMap<>();

        for (int i = 0; i < array.length; i++) {
            hashMap.put(String.valueOf(i + 1), array[i]);
        }

        return hashMap;
    }

}
