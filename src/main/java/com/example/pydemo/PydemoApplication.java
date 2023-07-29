package com.example.pydemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.pydemo.dao")
public class PydemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(PydemoApplication.class, args);
    }

}
