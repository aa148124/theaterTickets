package com.dkh.mq;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.dkh.mapper")
public class MQMain {
    public static void main(String[] args) {
        SpringApplication.run(MQMain.class, args);
    }
}
