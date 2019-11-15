package lxdemo.springcloud.systemAdmin.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"lxdemo.springcloud.systemAdmin.mapper"})
public class MybatisConfig {
}
