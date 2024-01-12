package com.example.RM_solution.solutionService.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifySubscriptionRequest {
    private long subscriptionId;
    private int subscriptionPeriod;
}
