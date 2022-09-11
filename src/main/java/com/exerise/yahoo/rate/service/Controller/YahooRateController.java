package com.exerise.yahoo.rate.service.Controller;

import com.exerise.yahoo.rate.service.model.YahooRateResponse;
import com.exerise.yahoo.rate.service.service.YahooRateService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class YahooRateController {
    @Autowired
    private YahooRateService yahooRateService;
    Logger logger = LoggerFactory.getLogger(YahooRateController.class);

    @SneakyThrows
    @GetMapping(value = "/getRate")
    public ResponseEntity<YahooRateResponse> getYahooRate () {
        String baseCcy = "JPY";
        String termCcy = "HKD";
        YahooRateResponse yahooRateResponse = yahooRateService.getFormYahoo(baseCcy, termCcy);
        return new ResponseEntity<>(yahooRateResponse, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "/getRate2")
    public ResponseEntity<YahooRateResponse> getYahooRate2 () {
        String baseCcy = "JPY";
        String termCcy = "HKD";
        YahooRateResponse yahooRateResponse = yahooRateService.getFormYahoo2(baseCcy, termCcy);
        return new ResponseEntity<>(yahooRateResponse, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping(value = "/getRateSync")
    public ResponseEntity<YahooRateResponse> getYahooRateSync () {
        String baseCcy = "JPY";
        String termCcy = "HKD";
        YahooRateResponse yahooRateResponse = yahooRateService.getFormYahooSync(baseCcy, termCcy);
        return new ResponseEntity<>(yahooRateResponse, HttpStatus.OK);
    }

}
