package com.guoyuhang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import tk.mybatis.spring.annotation.MapperScan;
//import tk.mybatis.spring.annotation.MapperScan;

/**
 * 描述:
 *
 * @author 国宇航
 * @create 2020-06-06 12:17
 */
@SpringBootApplication
@EnableEurekaClient //声明当前的工程是eureka客户端
@MapperScan(basePackages = {"com.guoyuhang.mapper"})
@EnableFeignClients
public class OrderApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }
}
