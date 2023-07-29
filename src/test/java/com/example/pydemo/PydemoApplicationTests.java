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

        HashMap<String, String> map = new HashMap<>();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");
        String[] fileds = new String[]{"PassengerId", "Name"};// 设置列英文名,即实体类里面对应的列名

        CsvUtil<Titanic> csvUtil = new CsvUtil<>();

        String splitStr = "@@@";
        String resultFilename = csvUtil.createCSVFile(titanicArrayList, fileds, map, filePath, fileName, splitStr);
        //System.out.println(resultFilename);


//        for (Titanic titanic : titanicArrayList) {
//            System.out.println(titanic.toString());
//        }


        System.out.println("Start");
        // python解释器的路径，
        String pyExecPath = "/Users/garlicv/anaconda3/envs/py3.7/bin/python";
        // python脚本的路径，
        String pyPath = "src/main/resources/py-file/plus.py";
        // 传入python脚本的参数
        String[] args1 = new String[]{pyExecPath, pyPath, resultFilename, splitStr, "源列2", "目标列"};
        String targetPath = filePath + "/";
        String cleanFilename = "result.csv";
        String execSh = pyExecPath +
                " " +
                pyPath +
                " " +
                "--file_path " +
                targetPath + resultFilename +
                " " +
                "--result_file_path " +
                targetPath + cleanFilename +
                " " +
                "--sep " +
                splitStr;

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

        System.out.println("End");

        CsvUtil.deleteFile(filePath, resultFilename);



    }

}
