package com.exerise.yahoo.rate.service.serviceImpl;

import com.exerise.yahoo.rate.service.config.YahooProperties;
import com.exerise.yahoo.rate.service.controller.YahooRateController;
import com.exerise.yahoo.rate.service.model.*;
import com.exerise.yahoo.rate.service.service.Impl.YahooRateServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class YahooRateServiceImplTest {
    @InjectMocks
    private YahooRateServiceImpl yahooRateService;
    @SpyBean
    private YahooRateServiceImpl yahooRateServicSpy;
    @Mock
    YahooProperties yahooProperties;
    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    @SneakyThrows
    public void setup() {
        MockitoAnnotations.initMocks(this);

        yahooProperties = new YahooProperties();
        yahooProperties.setEndpoint("https://query2.finance.yahoo.com/v7/finance/quote?formatted=true&fields=regularMarketPrice&symbols=JPYHKD=X");
        yahooRateService = new YahooRateServiceImpl(yahooProperties, restTemplate);
        QuoteResponse quoteResponse = new QuoteResponse();
        Result result = new Result();

        RegularMarketPrice price = new RegularMarketPrice();
        price.setFormattedRMP("0.12345");
        result.setRegularMarketPrice(price);
        QuotedResponse quotedResponse = new QuotedResponse();
        List<Result> results = new ArrayList<>();
        results.add(result);
        quoteResponse.setResults(results);

        quotedResponse.setQuoteResponse(quoteResponse);
        ResponseEntity<QuotedResponse> responseEntity = new ResponseEntity(quotedResponse, HttpStatus.OK);
        Mockito.when(this.restTemplate.getForEntity(yahooProperties.getEndpoint(), QuotedResponse.class))
        .thenReturn(responseEntity);

    }
    @Test
    @SneakyThrows
    public void testCreateFutureCall() {
        CompletableFuture<ResponseEntity<QuotedResponse>> resultAsync = yahooRateService.createFutureCall(yahooProperties.getEndpoint());
        ResponseEntity<QuotedResponse> result = resultAsync.get();
        assertEquals(("0.12345"), result.getBody().getQuoteResponse().getResults().get(0).getRegularMarketPrice().getFormattedRMP());
    }
    @Test
    @SneakyThrows
    public void testCreateFutureCallWithError() {
        doThrow(new RuntimeException()).when(yahooRateServicSpy).createFutureCall(any());
        assertThrows(Exception.class, ()-> yahooRateServicSpy.createFutureCall(yahooProperties.getEndpoint()));
    }
    @Test
    @SneakyThrows
    public void testGetQuotedFromYahooFinanceWithoutError() {
        ResponseEntity<QuotedResponse> response = yahooRateService.getQuotedFromYahooFinance(yahooProperties.getEndpoint());
        assertEquals("0.12345", response.getBody().getQuoteResponse().getResults().get(0).getRegularMarketPrice().getFormattedRMP());
    }
    @Test
    @SneakyThrows
    public void testGetQuotedFromYahooFinanceWithError() {
        Mockito.when(this.restTemplate.getForEntity(yahooProperties.getEndpoint(), QuotedResponse.class))
                .thenThrow(new RestClientException(""));
        ResponseEntity<QuotedResponse> response = yahooRateService.getQuotedFromYahooFinance(yahooProperties.getEndpoint());
        assertNull(response);
    }

    @Test
    @SneakyThrows
    public void testAsyncCallCompletesWithErrors() {
        Mockito.when(this.restTemplate.getForEntity(yahooProperties.getEndpoint(), QuotedResponse.class))
                .thenThrow(new RuntimeException());
        CompletableFuture<ResponseEntity<QuotedResponse>> resultAsync = yahooRateService.createFutureCall(yahooProperties.getEndpoint());
        ResponseEntity<QuotedResponse> result = resultAsync.get();
        assertNull(result);
    }
    @Test
    @SneakyThrows
    public void testGetRateFromYahooFinanceWithoutError() {
        YahooRateResponse yahooRateResponse = yahooRateServicSpy.getFormYahoo("JPY","HKD");
        verify(yahooRateServicSpy).getQuotedFromYahooFinance(any());
    }

}
