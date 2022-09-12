package com.exerise.yahoo.rate.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
public class YahooRateResponse {
    String ccyPair;
    String rate;
    Date quotedTimestamp;
    Date timestamp;
}
