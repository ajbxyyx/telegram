package com.ajbxyyx;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.ajbxyyx.dao.mapper")
@EnableKafka
public class Application {



    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }




}
