package com.exerise.yahoo.rate.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegularMarketPrice {
    @JsonProperty("fmt")
    private String formattedRMP;
}