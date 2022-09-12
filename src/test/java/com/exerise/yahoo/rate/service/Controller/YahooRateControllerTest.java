package com.exerise.yahoo.rate.service.controller;

import com.exerise.yahoo.rate.service.config.YahooProperties;
import com.exerise.yahoo.rate.service.model.YahooRateResponse;
import com.exerise.yahoo.rate.service.service.YahooRateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;

@SpringBootTest
public class YahooRateControllerTest {
    private YahooRateController yahooRateController;
    @Mock
    private YahooRateService yahooRateService;
    @Mock
    private YahooProperties yahooProperties;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        yahooRateController = new YahooRateController(yahooRateService, yahooProperties);
        YahooProperties yahooProperties = new YahooProperties();
        yahooProperties.setEndpoint("");
    }
    @Test
    @SneakyThrows
    public void testGetYahooRate() {
        YahooRateResponse mockResponse = new YahooRateResponse();
        Date date = new Date();
        mockResponse.setRate("0.1234");
        mockResponse.setCcyPair("JPYCNY");
        mockResponse.setTimestamp(date);
        mockResponse.setQuotedTimestamp(date);
        when(yahooRateService.getFormYahoo(any(), any(), anyBoolean())).thenReturn(mockResponse);
        ResponseEntity<YahooRateResponse> responseEntity = yahooRateController.getYahooRate();
        assertEquals(("0.1234"), responseEntity.getBody().getRate());
        assertEquals(date, responseEntity.getBody().getTimestamp());
        assertEquals(date, responseEntity.getBody().getQuotedTimestamp());
        assertEquals("JPYCNY", responseEntity.getBody().getCcyPair());
    }

    @Test
    @SneakyThrows
    public void testGetExceptionYahooRate() {
        when(yahooRateService.getFormYahoo(any(), any(), anyBoolean())).thenThrow(RuntimeException.class);
        assertThrows(Exception.class, () -> yahooRateController.getYahooRate());
    }

    @Test
    @SneakyThrows
    public void testGetNullYahooRate() {
        YahooRateResponse mockResponse = new YahooRateResponse();
        Date date = new Date();
        mockResponse.setRate(null);
        mockResponse.setTimestamp(date);
        mockResponse.setCcyPair("JPYCNY");
        mockResponse.setQuotedTimestamp(date);
        when(yahooRateService.getFormYahoo(any(), any(), anyBoolean())).thenReturn(mockResponse);
        ResponseEntity<YahooRateResponse> responseEntity = yahooRateController.getYahooRate();
        assertNull(responseEntity.getBody().getRate());
        assertEquals(date, responseEntity.getBody().getTimestamp());
        assertEquals(date, responseEntity.getBody().getQuotedTimestamp());
        assertEquals("JPYCNY", responseEntity.getBody().getCcyPair());
    }

    @Test
    public void testGetNullYahooRateWithCcyException() {
        when(yahooRateService.getFormYahoo(any(), any(), anyBoolean())).thenReturn(null);
        assertThrows(Exception.class, () -> yahooRateController.getYahooRateByCcyPair("TES","TEST"));
    }
    @Test
    public void testGetNullYahooRateException() {
        when(yahooRateService.getFormYahoo(any(), any(), anyBoolean())).thenReturn(null);
        assertThrows(Exception.class, () -> yahooRateController.getYahooRate());

    }
}
