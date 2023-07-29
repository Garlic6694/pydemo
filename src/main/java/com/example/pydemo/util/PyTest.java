package com.example.pydemo.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PyTest {

    public static void main(String[] args) {
        RunPy();
    }

    static void RunPy() {
        System.out.println("Start");
        // python脚本的绝对路径，在windows中用"\\"分隔，在Linux中用"/"分隔
        String pyPath = "src/main/resources/py-file/test1.py";

        // 传入python脚本的参数为”111“
        String[] args1 = new String[]{"python", pyPath, "111 222","xxx","yyy"};

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
}
