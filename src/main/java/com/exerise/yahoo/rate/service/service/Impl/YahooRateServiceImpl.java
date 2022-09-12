package com.exerise.yahoo.rate.service.service.Impl;

import com.exerise.yahoo.rate.service.config.YahooProperties;
import com.exerise.yahoo.rate.service.model.QuotedResponse;
import com.exerise.yahoo.rate.service.model.YahooRateResponse;
import com.exerise.yahoo.rate.service.service.YahooRateService;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@EnableConfigurationProperties(YahooProperties.class)
public class YahooRateServiceImpl implements YahooRateService {
    Logger logger = LoggerFactory.getLogger(YahooRateServiceImpl.class);
    private final YahooProperties yahooProperties;
    public RestTemplate restTemplate;

    @Autowired
    public YahooRateServiceImpl(YahooProperties yahooProperties,
                                RestTemplate restTemplate
                                ){
        this.yahooProperties = yahooProperties;
        this.restTemplate = restTemplate;
    }

    private final ExecutorService executorService = Executors.newFixedThreadPool(20);
    @Override
    @SneakyThrows
    public YahooRateResponse getFormYahoo(String baseCcy, String termCcy, Boolean defaultCcy) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(yahooProperties.getEndpoint());
        Map<String, String> uriVariable = new HashMap<>();
        YahooRateResponse yahooRateResponse = new YahooRateResponse();
        uriVariable.put("symbols", baseCcy + termCcy + "=X");
        uriVariable.put("fields", "regularMarketPrice");
        uriVariable.put("formatted", "true");
        for (Map.Entry<String, String> var : uriVariable.entrySet()) {
            uriComponentsBuilder.queryParam(var.getKey(), var.getValue());
        }

        String rateUrl = uriComponentsBuilder.build().toString();
        logger.info("Will Get rate from: {} ", rateUrl);

        CompletableFuture<ResponseEntity<QuotedResponse>> future = createFutureCall(rateUrl);
        try {
            ResponseEntity<QuotedResponse> res = future.get();
            String rate = res.getBody().getQuoteResponse().getResults().get(0).getRegularMarketPrice().getFormattedRMP();
            if(defaultCcy){
                BigDecimal multiply = new BigDecimal(1000);
                BigDecimal convertedRate = new BigDecimal(rate);
                convertedRate = multiply.multiply(convertedRate);
                rate = convertedRate.toString();
                termCcy = yahooProperties.getDefaultBaseCcy();
                baseCcy = yahooProperties.getDefaultTermCcy();
            }
            logger.info("Successfully get rate from Yahoo - {}{} : {}", baseCcy, termCcy, rate);

            if (res.getStatusCode() == HttpStatus.OK) {
                yahooRateResponse.setRate(rate);
                yahooRateResponse.setCcyPair(baseCcy + termCcy);
                Date time = new Date((long)res.getBody().getQuoteResponse().getResults().get(0).getRegularMarketTime().getRegularMarketTimestamp()*1000);
                yahooRateResponse.setQuotedTimestamp(time);
                yahooRateResponse.setTimestamp(new java.util.Date());
            }
            logger.info("yahooRateResponse - {}{} : {}", baseCcy, termCcy, yahooRateResponse);
        }
        catch (Exception e){
            logger.info("Failed GetFormYahoo: {}", e.getMessage());
            return null;
        }
        return yahooRateResponse;
    }
    @SneakyThrows
    public CompletableFuture<ResponseEntity<QuotedResponse>> createFutureCall (String rateUrl){
        return CompletableFuture.supplyAsync(() -> getQuotedFromYahooFinance(rateUrl), executorService);
    }
    @SneakyThrows
    public ResponseEntity<QuotedResponse> getQuotedFromYahooFinance(String rateUrl) {
        try {
            logger.info("Getting rate from: {} ", rateUrl);
            ResponseEntity<QuotedResponse> responseEntity = restTemplate.getForEntity(rateUrl, QuotedResponse.class);
            return responseEntity;
        } catch (Exception e) {
            logger.info("Failed GetQuotedFromYahooFinance: {}", e.getMessage());
            return null;
        }

    }
}
