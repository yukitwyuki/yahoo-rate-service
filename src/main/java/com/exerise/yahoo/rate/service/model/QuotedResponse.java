package com.exerise.yahoo.rate.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
@Getter
@Setter
@ToString
@NoArgsConstructor
public class QuotedResponse {
    @JsonProperty("quoteResponse")
    private QuoteResponse quoteResponse;


}
