package com.exerise.yahoo.rate.service.service;

import com.exerise.yahoo.rate.service.model.YahooRateResponse;
import lombok.SneakyThrows;

public interface YahooRateService {
    @SneakyThrows
    YahooRateResponse getFormYahoo(String baseCcy, String termCcy);
}
