package com.exerise.yahoo.rate.service.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class QuoteResponse {
    @JsonProperty("result")
    private List<Result> results;
    @JsonProperty("error")
    private String error;
}
