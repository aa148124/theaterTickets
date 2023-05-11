package com.dkh.sms;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.dkh.config.MpConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = {MpConfig.class})})
@EnableDiscoveryClient
@EnableConfigurationProperties(MybatisPlusProperties.class)
public class SmsMain {
    public static void main(String[] args) {
        SpringApplication.run(SmsMain.class, args);
    }
}
