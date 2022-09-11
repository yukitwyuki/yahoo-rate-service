package com.exerise.yahoo.rate.service.service.Impl;

import com.exerise.yahoo.rate.service.config.YahooProperties;
import com.exerise.yahoo.rate.service.model.QuotedResponse;
import com.exerise.yahoo.rate.service.model.YahooRateResponse;
import com.exerise.yahoo.rate.service.service.YahooRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@EnableConfigurationProperties(YahooProperties.class)
public class YahooRateServiceImpl implements YahooRateService {
    Logger logger = LoggerFactory.getLogger(YahooRateServiceImpl.class);

    private YahooProperties yahooProperties;
    private RestTemplate restTemplate = new RestTemplate();

    @Autowired
    public YahooRateServiceImpl(YahooProperties yahooProperties
                                ){
        this.yahooProperties = yahooProperties;
    }

    private ExecutorService executorService = Executors.newFixedThreadPool(20);
    private ExecutorService executorService2 = Executors.newFixedThreadPool(15);
    @Override
    public YahooRateResponse getFormYahoo2(String baseCcy, String termCcy) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(yahooProperties.getEndpoint());

        Map<String, String> uriVariable = new HashMap<>();
        YahooRateResponse yahooRateResponse = new YahooRateResponse();
        uriVariable.put("symbols", baseCcy + termCcy + "=X");
        uriVariable.put("fields", "regularMarketPrice");
        for (Map.Entry<String, String> var : uriVariable.entrySet()) {
            uriComponentsBuilder.queryParam(var.getKey(), var.getValue());
        }

        String rateUrl = uriComponentsBuilder.build().toString();
        logger.info(rateUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        CompletableFuture<ResponseEntity<QuotedResponse>> future = createFutureCall2(rateUrl, entity);
        try {
            ResponseEntity<QuotedResponse> res = future.get();
            logger.info(res.getBody().getQuoteResponse().getResults()[0].getRegularMarketPrice().toString());
            if (res.getStatusCode() == HttpStatus.OK) {
                yahooRateResponse.setRate(res.getBody().getQuoteResponse().getResults()[0].getRegularMarketPrice());
            }
        }
        catch (Exception e){
            logger.info("getFormYahoo: {}", e.getMessage());
            return null;
        }
        return yahooRateResponse;
    }
    public CompletableFuture<ResponseEntity<QuotedResponse>> createFutureCall2 (String rateUrl, HttpEntity<?> entity){
        return CompletableFuture.supplyAsync(() -> getQuotedFromYahooFinance(rateUrl, entity), executorService2);
    }
    @Override
    public YahooRateResponse getFormYahoo(String baseCcy, String termCcy) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(yahooProperties.getEndpoint());

        Map<String, String> uriVariable = new HashMap<>();
        YahooRateResponse yahooRateResponse = new YahooRateResponse();
        uriVariable.put("symbols", baseCcy + termCcy + "=X");
        uriVariable.put("fields", "regularMarketPrice");
        for (Map.Entry<String, String> var : uriVariable.entrySet()) {
            uriComponentsBuilder.queryParam(var.getKey(), var.getValue());
        }

        String rateUrl = uriComponentsBuilder.build().toString();
        logger.info(rateUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);

        CompletableFuture<ResponseEntity<QuotedResponse>> future = createFutureCall(rateUrl, entity);
        try {
            ResponseEntity<QuotedResponse> res = future.get();
            logger.info(res.getBody().getQuoteResponse().getResults()[0].getRegularMarketPrice().toString());
            if (res.getStatusCode() == HttpStatus.OK) {
                yahooRateResponse.setRate(res.getBody().getQuoteResponse().getResults()[0].getRegularMarketPrice());
            }
        }
        catch (Exception e){
            logger.info("getFormYahoo: {}", e.getMessage());
            return null;
        }
        return yahooRateResponse;
    }
    public CompletableFuture<ResponseEntity<QuotedResponse>> createFutureCall (String rateUrl, HttpEntity<?> entity){
        return CompletableFuture.supplyAsync(() -> getQuotedFromYahooFinance(rateUrl, entity), executorService);
    }
    public YahooRateResponse getFormYahooSync(String baseCcy, String termCcy) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder.fromUriString(yahooProperties.getEndpoint());

        Map<String, String> uriVariable = new HashMap<>();
        YahooRateResponse yahooRateResponse = new YahooRateResponse();
        uriVariable.put("symbols", baseCcy + termCcy + "=X");
        uriVariable.put("fields", "regularMarketPrice");
        for (Map.Entry<String, String> var : uriVariable.entrySet()) {
            uriComponentsBuilder.queryParam(var.getKey(), var.getValue());
        }

        String rateUrl = uriComponentsBuilder.build().toString();
        logger.info(rateUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<?> entity = new HttpEntity<>(headers);
        ResponseEntity<QuotedResponse> res = getQuotedFromYahooFinance(rateUrl, entity);

        logger.info(res.getBody().getQuoteResponse().getResults()[0].getRegularMarketPrice().toString());
        if (res.getStatusCode() == HttpStatus.OK) {
            yahooRateResponse.setRate(res.getBody().getQuoteResponse().getResults()[0].getRegularMarketPrice());
        }

        return yahooRateResponse;
    }
    public ResponseEntity<QuotedResponse> getQuotedFromYahooFinance(String rateUrl, HttpEntity<?> entity) {
        try {
            ResponseEntity<QuotedResponse> responseEntity = restTemplate.exchange(rateUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<QuotedResponse>() {
                    });
            logger.info(responseEntity.getBody().toString());
            return responseEntity;
        } catch (Exception e) {
            logger.info("getQuotedFromYahooFinance: {}", e.getMessage());
            return null;
        }

    }
}
