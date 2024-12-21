package com.lysero.fraud;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
@Slf4j
public class FraudController {

    private final FraudCheckService fraudCheckService;

    @GetMapping(path = "{playerId}")
    public FraudCheckResponse isFraudster(
            @PathVariable("playerId") UUID playerId) {
        boolean isFraudulentCustomer = fraudCheckService.
                isFraudulentCustomer(playerId);
        log.info("fraud check request for customer {}", playerId);

        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
