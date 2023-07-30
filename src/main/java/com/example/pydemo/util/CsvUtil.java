package com.example.pydemo.util;

import com.example.pydemo.model.Titanic;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

/**
 * 文件操作
 *
 * @Author GarlicV
 * @Data 2023年7月29日14:21:39
 */


public class CsvUtil<T> {


    /**
     * 生成为CVS文件
     *
     * @param exportData 源数据List
     * @param fields
     * @param map        csv文件的列表头map
     * @param outPutPath 文件路径
     * @param fileName   文件名称
     * @return
     */
    @SuppressWarnings("rawtypes")
    public String createCSVFile(List<T> exportData, String[] fields, HashMap map, String outPutPath, String fileName, String splitStr) {
        File csvFile = null;
        BufferedWriter csvFileOutputStream = null;
        try {
            File file = new File(outPutPath);
            if (!file.exists()) {
                file.mkdir();
            }
            // 定义文件名格式并创建
            csvFile = File.createTempFile(fileName, ".csv",
                    new File(outPutPath));
            //System.out.println("csvFile：" + csvFile);
            // UTF-8使正确读取分隔符","
            csvFileOutputStream = new BufferedWriter(new OutputStreamWriter(
                    Files.newOutputStream(csvFile.toPath()), StandardCharsets.UTF_8), 1024);
            //System.out.println("csvFileOutputStream：" + csvFileOutputStream);
            // 写入文件头部
            for (Iterator propertyIterator = map.entrySet().iterator(); propertyIterator.hasNext(); ) {
                java.util.Map.Entry propertyEntry = (java.util.Map.Entry) propertyIterator.next();
                csvFileOutputStream.write(propertyEntry.getValue() != null ? new String(((String) propertyEntry.getValue()).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8) : "");
                if (propertyIterator.hasNext()) {
                    csvFileOutputStream.write(splitStr);
                }
                //System.out.println(new String(((String) propertyEntry.getValue()).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8));
            }
            csvFileOutputStream.write("\r\n");
            // 写入文件内容,
            // ============ //第一种格式：Arraylist<实体类>填充实体类的基本信息==================
            for (int j = 0; exportData != null && !exportData.isEmpty() && j < exportData.size(); j++) {
                T t = (T) exportData.get(j);
                Class clazz = t.getClass();
                String[] contents = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    String filedName = toUpperCaseFirstOne(fields[i]);
                    Method method = clazz.getMethod(filedName);
                    method.setAccessible(true);
                    Object obj = method.invoke(t);
                    String str = String.valueOf(obj);
                    if (str == null || str.equals("null"))
                        str = "";
                    contents[i] = str;

                }

                for (int i = 0; i < contents.length; i++) {
                    if (i != contents.length - 1) {
                        csvFileOutputStream.write(contents[i]);
                        csvFileOutputStream.write("@@@");
                    } else {
                        csvFileOutputStream.write(contents[i]);
                    }

                }

                csvFileOutputStream.write("\r\n");
            }
            csvFileOutputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                csvFileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return csvFile.getName();
    }

    /**
     * 下载文件
     *
     * @param response
     * @param csvFilePath 文件路径
     * @param fileName    文件名称
     * @throws IOException
     */
    public static void exportFile(HttpServletResponse response,
                                  String csvFilePath, String fileName) throws IOException {
        response.setContentType("application/csv;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment;  filename="
                + new String(fileName.getBytes(StandardCharsets.UTF_8), "ISO8859-1"));
        // URLEncoder.encode(fileName, "UTF-8")

        InputStream in = null;
        try {
            in = new FileInputStream(csvFilePath);
            int len = 0;
            byte[] buffer = new byte[1024];
            response.setCharacterEncoding("UTF-8");
            OutputStream out = response.getOutputStream();
            while ((len = in.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 删除该目录filePath下的所有文件
     *
     * @param filePath 文件目录路径
     */
    public static void deleteFiles(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (File value : files) {
                if (value.isFile()) {
                    value.delete();
                }
            }
        }
    }

    /**
     * 删除单个文件
     *
     * @param filePath 文件目录路径
     * @param fileName 文件名称
     */
    public static boolean deleteFile(String filePath, String fileName) {
        File file = new File(filePath + "/" + fileName);
        if (file.exists()) {
            System.out.println("delete file success " + fileName);
            return file.delete();
        } else {
            System.out.println("file not exists");
        }
        return false;
    }

    /**
     * 测试数据
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static void main(String[] args) {
        // =======改成list的格式，支持（Arraylist传入实体类的形式），改造的方法============
        ArrayList<Titanic> titanicArrayList = new ArrayList<>();
        Titanic titanic = new Titanic();
        titanic.setId(123);
        titanic.setName("tom1");

        Titanic titanic1 = new Titanic();
        titanic1.setId(124);
        titanic1.setName("tom2");
        titanicArrayList.add(titanic);
        titanicArrayList.add(titanic1);


        HashMap<String, String> map = new HashMap<>();
        map.put("1", "第一列");
        map.put("2", "第二列");
        map.put("3", "第三列");
        map.put("4", "第四列");

        String rootPath = "src/main/resources/";

        String filePath = rootPath + "csv-file";

        String fileName = "csv_file_";
        String[] fileds = new String[]{"PassengerId", "Name"};// 设置列英文名,即实体类里面对应的列名

        CsvUtil csvUtil = new CsvUtil<Titanic>();

        String splitStr = "@@@";
        String resultFilename = csvUtil.createCSVFile(titanicArrayList, fileds, map, filePath, fileName, splitStr);
        System.out.println(resultFilename);


        System.out.println("Start");


        String pyPath = rootPath + "py-file/plus.py";

        // 传入python脚本的参数为”111“
        String[] args1 = new String[]{"python", pyPath, "111 222", "xxx", "yyy"};

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

//        boolean result = CsvUtil.deleteFile(filePath, resultFilename);
//        System.out.println(result);

    }

    /**
     * 将第一个字母转换为大写字母并和get拼合成方法
     */
    private static String toUpperCaseFirstOne(String origin) {
        StringBuilder sb = new StringBuilder(origin);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        sb.insert(0, "get");
        return sb.toString();
    }
}