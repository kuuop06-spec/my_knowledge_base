package com.kuuop.my_knowlege_base;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.kuuop.my_knowlege_base.mapper")
public class MyKnowlegeBaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyKnowlegeBaseApplication.class, args);
    }

}
