package com.lysero.fraud;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/fraud-check")
@AllArgsConstructor
public class FraudController {

    private final FraudCheckService fraudCheckService;

    @GetMapping(path = "{playerId}")
    public FraudCheckResponse isFraudster(
            @PathVariable("playerId") UUID playerId) {
        boolean isFraudulentCustomer = fraudCheckService.
                isFraudulentCustomer(playerId);
        return new FraudCheckResponse(isFraudulentCustomer);
    }
}
