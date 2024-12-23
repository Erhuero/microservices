package com.lysero.clients.fraud;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(
        value = "fraud"
)
public interface FraudClient {
    @GetMapping(path = "api/v1/fraud-check/{deckId}")
    FraudCheckResponse isFraudster(
            @PathVariable("deckId") UUID deckId);
}
