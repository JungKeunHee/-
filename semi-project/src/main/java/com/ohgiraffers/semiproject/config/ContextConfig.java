package com.ohgiraffers.semiproject.config;

import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("com.ohgiraffers.semiproject")
@MapperScan(basePackages = "com.ohgiraffers.semiproject", annotationClass = Mapper.class)
public class ContextConfig {
}
