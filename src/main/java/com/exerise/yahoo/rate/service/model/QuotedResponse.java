package com.exerise.yahoo.rate.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuotedResponse {
    @JsonProperty("quoteResponse")
    private QuoteResponse quoteResponse;


}
