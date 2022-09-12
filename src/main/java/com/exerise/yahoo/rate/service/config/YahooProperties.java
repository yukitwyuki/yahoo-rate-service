package com.exerise.yahoo.rate.service.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration("yahooProperties")
@ConfigurationProperties(prefix="yahoo")
public class YahooProperties {
    public String endpoint;
    public String query;
    public String defaultBaseCcy;
    public String defaultTermCcy;
}
