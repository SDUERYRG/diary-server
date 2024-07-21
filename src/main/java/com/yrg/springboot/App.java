package com.yrg.springboot;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
//开启事务管理
@EnableTransactionManagement
//jetcache启用缓存开关
@EnableCreateCacheAnnotation
//允许方法缓存，需要指定包
@EnableMethodCache(basePackages = "com.yrg.springboot")
public class App 
{
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}