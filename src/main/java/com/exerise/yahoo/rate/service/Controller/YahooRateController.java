package com.exerise.yahoo.rate.service.controller;

import com.exerise.yahoo.rate.service.config.YahooProperties;
import com.exerise.yahoo.rate.service.model.YahooRateResponse;
import com.exerise.yahoo.rate.service.service.YahooRateService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableConfigurationProperties(YahooProperties.class)
public class YahooRateController {
    private final YahooRateService yahooRateService;
    private final YahooProperties yahooProperties;
    Logger logger = LoggerFactory.getLogger(YahooRateController.class);

    public YahooRateController(YahooRateService yahooRateService,
                               YahooProperties yahooProperties){
        this.yahooRateService = yahooRateService;
        this.yahooProperties = yahooProperties;
    }
    @SneakyThrows
    @GetMapping(value = "/getRate")
    public ResponseEntity<YahooRateResponse> getYahooRate() {
        String baseCcy = yahooProperties.getDefaultBaseCcy();
        String termCcy = yahooProperties.getDefaultTermCcy();
        logger.info("Get {}.{} from Yahoo Finance.", baseCcy, termCcy);
        YahooRateResponse response = yahooRateService.getFormYahoo(baseCcy, termCcy, true);
        if(response == null)
            throw new Exception("Cannot get rate of Ccy Pair!");
        logger.info("Get last updated rate {}.{} from Yahoo Finance - {}.", baseCcy, termCcy, response.getRate());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @SneakyThrows
    @GetMapping(value = "/getRate/{baseCcy}/{termCcy}")
    public ResponseEntity<YahooRateResponse> getYahooRateByCcyPair(@PathVariable String baseCcy,
                                                                   @PathVariable String termCcy) {
        logger.info("Get {}.{} from Yahoo Finance.", baseCcy, termCcy);
        YahooRateResponse response = yahooRateService.getFormYahoo(baseCcy, termCcy, false);
        if(response == null)
            throw new Exception("Cannot get rate of Ccy Pair!");
        logger.info("Get last updated rate {}.{} from Yahoo Finance - {}.", baseCcy, termCcy, response.getRate());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
