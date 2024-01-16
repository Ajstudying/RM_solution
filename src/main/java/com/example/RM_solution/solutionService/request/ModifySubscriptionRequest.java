package com.example.RM_solution.solutionService.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ModifySubscriptionRequest {
    //구독 만료일 수정 리퀘스트
    private long subscriptionId;
    private int subscriptionPeriod;
}
