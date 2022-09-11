package com.exerise.yahoo.rate.service;

import com.exerise.yahoo.rate.service.config.YahooProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(YahooProperties.class)
public class YahooRateServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(YahooRateServiceApplication.class, args);
    }

}
