package com.lysero.fraud;

import com.lysero.clients.fraud.FraudCheckResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudController {

    private final FraudCheckService fraudCheckService;

    @GetMapping(path = "{playerId}")
    public FraudCheckResponse isFraudster(@PathVariable("playerId") UUID playerId) {
        boolean isFraudulentCustomer = fraudCheckService.isFraudulentCustomer(playerId);
        log.info("fraud check request for deck {}", playerId);
        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
