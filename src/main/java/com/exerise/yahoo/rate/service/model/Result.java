package com.exerise.yahoo.rate.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
public class Result{
    @JsonProperty("region")
    private String region;

    @JsonProperty("quoteType")
    private String quoteType;

    @JsonProperty("quoteSourceName")
    private String quoteSourceName;

    @JsonProperty("regularMarketPrice")
    private BigDecimal regularMarketPrice;

    @JsonProperty("marketState")
    private String marketState;

    @JsonProperty("exchange")
    private String exchange;

    @JsonProperty("market")
    private String market;

    @JsonProperty("symbol")
    private String symbol;

}
